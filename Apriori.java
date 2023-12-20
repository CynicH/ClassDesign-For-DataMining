import java.util.*;

public class Apriori {
    //定义单行数据
    public static String[] item1, item2, item3, item4, item5, item6, item7, item8, item9, item10, item11, item12, item13, item14;
    //定义数据集
    public static List<String[]> dataList = new ArrayList<>();
    //定义最小支持度
    public static double minSupport = 0.5;
    //定义最小支持数
    public static double minSupportCount = 0.0;
    //定义最小置信度
    public static double minConfidence = 0.5;
    //存放所有频繁项目集
    public static Map<String[],Integer> frequentItems =new HashMap<>();
    //存放一次循环的频繁项目集
    public static Map<String[],Integer> frequentItemsI =new HashMap<>();
    //存放所有的关联规则
    public static  Map<String[],String[]> Association=new HashMap<>();
    //得到所有可能出现的字母
    public static List<String> InitAppearItem(List<String[]> items){
        List appearItem=new ArrayList<String>();int index=0;
        for(int i=0;i<items.size();i++){
            String data[]=items.get(i);
            for(int j=0;j< data.length;j++){
                if(!appearItem.contains(data[j])){
                    appearItem.add(data[j]);
                }
            }
        }
        return  appearItem;
    }
    // 生成固定大小的子集
    private static List<List<String>> generateSubsets(List<String> set, int subsetSize) {
        List<List<String>> subsets = new ArrayList<>();
        generateSubsetsHelper(set, subsetSize, 0, new ArrayList<>(), subsets);
        return subsets;
    }
    private static void generateSubsetsHelper(List<String> set, int subsetSize, int start,
                                              List<String> currentSubset, List<List<String>> subsets) {
        if (subsetSize == 0) {
            subsets.add(new ArrayList<>(currentSubset));
            return;
        }

        for (int i = start; i < set.size(); i++) {
            currentSubset.add(set.get(i));
            generateSubsetsHelper(set, subsetSize - 1, i + 1, currentSubset, subsets);
            currentSubset.remove(currentSubset.size() - 1);
        }
    }
    //去除不符合的
    public static void mining(Map<String[],Integer> items){
        // 使用迭代器遍历 Map
        Iterator<Map.Entry<String[], Integer>> iterator = items.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String[], Integer> entry = iterator.next();
            // 如果值小于 minSupport，则删除该元素
            if (entry.getValue() < minSupportCount) {
                iterator.remove();
            }
        }

    }
    public static void main(String[] args) {
        initDataList();
        //得到所有可能的字母
        List Items=InitAppearItem(dataList);
        //计算第一次频繁项目集
        for(int i=0;i<Items.size();i++){
            String [] item={Items.get(i).toString()};
            frequentItemsI.put(item,getCountByConfidence(item));
        }
        //去除不符合的
        mining(frequentItemsI);
        //符合的放入所有频繁项目集合
        frequentItems.putAll(frequentItemsI);
        //循环进行查找
        for(int size=2;size<Items.size();size++){
            //得到现在的频繁项目集的所有字母
            List<String> list = InitAppearItem(new ArrayList<>(frequentItemsI.keySet()));
            //清除上一次循环中得到的频繁项目集
            frequentItemsI.clear();
            //得到上一次频繁项目集的子集
            List<List<String>> SonList=generateSubsets(list,size);
            //得到项目集的出现次数
            for(int i=0;i<SonList.size();i++){
                String[] stringArray = new String[SonList.get(i).size()];
                stringArray = SonList.get(i).toArray(stringArray);
                frequentItemsI.put(stringArray,getCountByConfidence(stringArray));
            }
            //筛选频繁项目集
            mining(frequentItemsI);
            //将频繁项目集加入总的频繁项目集的集合中
            frequentItems.putAll(frequentItemsI);
        }
        //打印Map
        // 遍历键, 根据键获取值
        Set<String[]> keys1 = frequentItems.keySet();
        for (String[] key: keys1) {
            int value = frequentItems.get(key);
            System.out.println(Arrays.toString(key) + "--->" +value);
        }
//挖掘关联规则
        // 遍历 Map 中的每对键值对
        for (Map.Entry<String[],Integer>  entry1 : frequentItems.entrySet()) {
            String[] key1 = entry1.getKey();
            // 将当前元素与其他元素进行比较
            for (Map.Entry<String[],Integer> entry2 : frequentItems.entrySet()) {
                String key2[] = entry2.getKey();
                // 将数组转换为 Set
                Set<String> set1 = new HashSet<>(Arrays.asList(key1));
                Set<String> set2 = new HashSet<>(Arrays.asList(key2));
                // 求并集
                Set<String> unionSet = new HashSet<>(set1);
                unionSet.addAll(set2);
                // 将结果转换为数组
                String[] together = unionSet.toArray(new String[0]);
                if(isConfidence(together,key1)&&!key1.equals(key2)){
                    Association.put(key2,key1);
                }
            }
        }
        Set<String[]> keys = Association.keySet();
        for (String[] key: keys) {
            String[] value = Association.get(key);
            System.out.println(Arrays.toString(key) + "--->" +Arrays.toString(value));
        }
    }
    //计算是否是强关联规则
    public static boolean isConfidence(String[] together, String[] value){
        double confidence = getCountByConfidence(value) * 1.0 / getCountByConfidence(together);
        if (confidence >= minConfidence) return true;
        return false;
    }
    //计算子集出现的次数
    public static Integer getCountByConfidence(String[] subSetStr){
        //子集出现的次数
        int count = 0;
        for (String[] dataItem : dataList) {
            //用于表示是否出现过dataItem中的内容
            int index = -1;
            for (int i = 0; i < subSetStr.length; i++) {
                //有一个字母过，则index>0，若A没出现过，则index=-1
                index = Arrays.binarySearch(dataItem, subSetStr[i]);
                if (index < 0) break;
            }
            //所有字母都出现
            if (index >= 0){
                count++;
            }
        }
        return count;
    }
    //init
    public static void initDataList() {
        //初始化单行数据
        item1 = new String[]{"A", "B", "C", "D", "E", "F", "G"};
        item2 = new String[]{"A", "B", "C", "D", "E", "H"};
        item3 = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
        item4 = new String[]{"A", "B", "C", "G", "H"};
        item5 = new String[]{"A", "B", "C", "D", "G", "H"};
        item6 = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
        item7 = new String[]{"A", "B", "C", "D", "E", "F", "G"};
        item8 = new String[]{"A", "B", "C", "E", "G", "H"};
        item9 = new String[]{"A", "B", "C", "D", "E", "F", "H"};
        item10 = new String[]{"C", "D", "E", "F", "G", "H"};
        item11 = new String[]{"A", "B", "C", "D", "G", "H"};
        item12 = new String[]{"A", "C", "D", "E", "F", "G", "H"};
        item13 = new String[]{"A", "B", "C", "E", "F", "G", "H"};
        item14 = new String[]{"B", "C", "E", "F", "G", "H"};
        //初始化数据集
        dataList.add(item1);
        dataList.add(item2);
        dataList.add(item3);
        dataList.add(item4);
        dataList.add(item5);
        dataList.add(item6);
        dataList.add(item7);
        dataList.add(item8);
        dataList.add(item9);
        dataList.add(item10);
        dataList.add(item11);
        dataList.add(item12);
        dataList.add(item13);
        dataList.add(item14);
        //赋值给最小支持的数
        minSupportCount = dataList.size() * minSupport;
    }
}
//public class Main {
//
//        //定义单行数据
//        public static String[] item1, item2, item3, item4, item5, item6, item7, item8, item9, item10, item11, item12, item13, item14;
//        //定义数据集
//        public static List<String[]> dataList = new ArrayList<>();
//        //定义最小支持度
//        public static double minSupport = 0.5;
//        //定义最小支持数
//        public static double minSupportCount = 0.0;
//        //定义最小置信度
//        public static double minConfidence = 0.5;
////        public static class frequentItem{
////                public frequentItem(Integer number, String[] data) {
////                        this.number = number;
////                        this.data = data;
////                }
////
////                public Integer getNumber() {
////                        return number;
////                }
////
////                public void setNumber(Integer number) {
////                        this.number = number;
////                }
////
////                public String[] getData() {
////                        return data;
////                }
////
////                public void setData(String[] data) {
////                        this.data = data;
////                }
////
////                @Override
////                public String toString() {
////                        return "frequentItem{" +
////                                "number=" + number +
////                                ", data=" + Arrays.toString(data) +
////                                '}';
////                }
////
////                Integer number;
////                String [] data ;
////
////
////        }
//        //存放所有频繁项目集
//        public static Map<String[],Integer> frequentItems =new HashMap<>();
//        //存放一次循环的频繁项目集
//        public static Map<String[],Integer> frequentItemsI =new HashMap<>();
////获取一个字母的出现次数
////        public static Integer getOne(String target){
////                int index=0;
////                Map<Integer,String> map=new HashMap<>();
////                for(int i=0;i<dataList.size();i++){
////                      String[] data=dataList.get(i);
////                        for(int j=0;j<data.length;j++){
////                                if(data[j].equals(target))
////                                {
////                                        index++;
////                                }
////                        }
////                }
////return index;
////        }
//        //得到所有可能出现的字母
//        public static List InitAppearItem(){
//               List appearItem=new ArrayList<String>();int index=0;
//                for(int i=0;i<dataList.size();i++){
//                        String data[]=dataList.get(i);
//                        for(int j=0;j< data.length;j++){
//                                if(!appearItem.contains(data[j])){
//                                        appearItem.add(data[j]);
//                                }
//                        }
//                }
//                return  appearItem;
//        }
//        //去除不符合的
//        public static void mining(){
//                // 使用迭代器遍历 Map
//                Iterator<Map.Entry<String[], Integer>> iterator = frequentItems.entrySet().iterator();
//                while (iterator.hasNext()) {
//                        Map.Entry<String[], Integer> entry = iterator.next();
//                        // 如果值小于 minSupport，则删除该元素
//                        if (entry.getValue() < minSupportCount) {
//                                iterator.remove();
//                        }
//                }
//
//        }
//        public static void main(String[] args) {
//                initDataList();
//                //得到所有可能的字母
//                List Items=InitAppearItem();
////                System.out.println(Items);
//                //
//                for(int size=0;size<Items.size();size++){
//                        frequentItemsI.clear();
//                        Set<String[]> son= frequentItemsI.keySet();
//                        List<String> Son=new ArrayList<>();
//                        for (String[] key: son) {
//                                Son.add(key[0]);
//                        }
//
//
//
//                }
//                //
//                //计算第一次频繁项目集
//for(int i=0;i<Items.size();i++){
//        String [] item={Items.get(i).toString()};
////        System.out.println(Items.get(i).toString());
////      System.out.println(Arrays.toString(item)+getCountByConfidence(item));
//frequentItems.put(item,getCountByConfidence(item));frequentItemsI.put(item,getCountByConfidence(item));
//}
//                //去除不符合的
//                mining();
////得到子集
//Set<String[]> son= frequentItemsI.keySet();
//List<String> Son=new ArrayList<>();
//                for (String[] key: son) {
//    Son.add(key[0]);
//                }
//                //大小为2的子集
//                List<List<String>> list=generateSubsets(Son,2);
//                for(int i=0;i<list.size();i++){
//                        String[] stringArray = new String[list.get(i).size()];
//                        stringArray = list.get(i).toArray(stringArray);
//                        frequentItems.put(stringArray,getCountByConfidence(stringArray));
//                }
//                mining();
//
//                }
//
//////打印Map
////                // 遍历键, 根据键获取值
////                Set<String[]> keys = frequentItems.keySet();
////                for (String[] key: keys) {
////                        int value = frequentItems.get(key);
////                        System.out.println(Arrays.toString(key) + "--->" +value);
////                }
//
//
//
//        // 生成固定大小的子集
//        private static List<List<String>> generateSubsets(List<String> set, int subsetSize) {
//                List<List<String>> subsets = new ArrayList<>();
//                generateSubsetsHelper(set, subsetSize, 0, new ArrayList<>(), subsets);
//                return subsets;
//        }
//
//        private static void generateSubsetsHelper(List<String> set, int subsetSize, int start,
//                                                  List<String> currentSubset, List<List<String>> subsets) {
//                if (subsetSize == 0) {
//                        subsets.add(new ArrayList<>(currentSubset));
//                        return;
//                }
//
//                for (int i = start; i < set.size(); i++) {
//                        currentSubset.add(set.get(i));
//                        generateSubsetsHelper(set, subsetSize - 1, i + 1, currentSubset, subsets);
//                        currentSubset.remove(currentSubset.size() - 1);
//                }
//        }
//
//
//
////计算置信度
//        public static boolean isConfidence(String[] subSetStr1, String[] value){
//                double confidence = getCountByConfidence(value) * 1.0 / getCountByConfidence(subSetStr1);
//                if (confidence >= minConfidence) return true;
//                return false;
//        }
////计算子集出现的次数
//        public static Integer getCountByConfidence(String[] subSetStr){
//                int count = 0; //存放子集在数据集中出现的次数
//                for (String[] dataItem : dataList) {
//                        //比如在数据集第一项{A,B,C,D,E,F,G}中是否出现过A,B.C
//                        int index = -1;
//                        for (int i = 0; i < subSetStr.length; i++) {
//                                //比如A出现过，则index>0，若A没出现过，则index=-1
//                                index = Arrays.binarySearch(dataItem, subSetStr[i]);
//                                if (index < 0) break;
//                        }
//                        if (index >= 0){ //出现过ABC
//                                count++;
//                        }
//                }
//                return count;
//        }
//
//
//
//
//
//
//
//
//
//
////init
//        public static void initDataList() {
//                //初始化单行数据
//                item1 = new String[]{"A", "B", "C", "D", "E", "F", "G"};
//                item2 = new String[]{"A", "B", "C", "D", "E", "H"};
//                item3 = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
//                item4 = new String[]{"A", "B", "C", "G", "H"};
//                item5 = new String[]{"A", "B", "C", "D", "G", "H"};
//                item6 = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
//                item7 = new String[]{"A", "B", "C", "D", "E", "F", "G"};
//                item8 = new String[]{"A", "B", "C", "E", "G", "H"};
//                item9 = new String[]{"A", "B", "C", "D", "E", "F", "H"};
//                item10 = new String[]{"C", "D", "E", "F", "G", "H"};
//                item11 = new String[]{"A", "B", "C", "D", "G", "H"};
//                item12 = new String[]{"A", "C", "D", "E", "F", "G", "H"};
//                item13 = new String[]{"A", "B", "C", "E", "F", "G", "H"};
//                item14 = new String[]{"B", "C", "E", "F", "G", "H"};
//
//                //初始化数据集
//                dataList.add(item1);
//                dataList.add(item2);
//                dataList.add(item3);
//                dataList.add(item4);
//                dataList.add(item5);
//                dataList.add(item6);
//                dataList.add(item7);
//                dataList.add(item8);
//                dataList.add(item9);
//                dataList.add(item10);
//                dataList.add(item11);
//                dataList.add(item12);
//                dataList.add(item13);
//                dataList.add(item14);
//
//                //赋值给最小支持的数
//                minSupportCount = dataList.size() * minSupport;
//        }
//
//}