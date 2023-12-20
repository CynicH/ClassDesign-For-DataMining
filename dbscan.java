package org.example;
import com.alibaba.excel.EasyExcel;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class dbscan {
    // 定义初始数据集
    public static List<point> dataList = new ArrayList<>();
    // 定义半径ibselo
    public static double e = 2.0;
    // 定义核心对象内的最少数目
    public static int minPoints = 3;
    //存放核心点
    public static List<point> minPoint=new ArrayList<>();
    //需要返回的簇

    static class point{
        public Integer getInIncluded() {
            return inIncluded;
        }

        public void setInIncluded(Integer inIncluded) {
            this.inIncluded = inIncluded;
        }

        private Integer inIncluded=0;
        private Integer cluster=0;
        private Double x;
        private Double y;
        private Integer isMin=0;
        public point(Integer cluster, Double x, Double y, Integer isMin) {
            this.cluster = cluster;
            this.x = x;
            this.y = y;
            this.isMin = isMin;
        }

        public Integer getIsMin() {
            return isMin;
        }

        public void setIsMin(Integer isMin) {
            this.isMin = isMin;
        }

        public point(Integer cluster, Double x, Double y) {
            this.cluster = cluster;
            this.x = x;
            this.y = y;
        }

        public Integer getCluster() {
            return cluster;
        }

        public void setCluster(Integer cluster) {
            this.cluster = cluster;
        }

        @Override
        public String toString() {
            return "point{" +
                    "cluster=" + cluster +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }

        public point(Double x, Double y) {
            this.x = x;
            this.y = y;
        }

        public Double getX() {
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }

        public Double getY() {
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }
        public double manhattanDistance(point other) {
            return Math.abs(this.x - other.getX()) + Math.abs(this.y - other.getY());
        }
        public point() {
        }
    }
    //得到可到达的点
    public static List<point> getReach(point p) {
        List<point> canReach=new ArrayList<>();
        for (point point : dataList) {
            if(point.manhattanDistance(p)<=e)
                canReach.add(point);
        }
//        System.out.println(p+" 可到达的点有"+canReach);
        return canReach;
    }
    //判断聚类中的核心点数目
    public static List<Integer> getMin(List<point> list) {
        List<Integer> minIndex=new ArrayList<>();
        for(point point:list){
            if (minPoint.contains(point)){
                minIndex.add(dataList.indexOf(point));
            }
        }
        return minIndex;
    }
    //得到一个点的包含的核心点（直接）
    public static List<point> getReachMin(point point) {
       List<point> list= getReach(point);
        List<point> includeMin = list.stream()
                .filter(minPoint::contains)
                .collect(Collectors.toList());
return includeMin;
    }
    //得到一个点的包含的核心点（全部）
    public static List<point> getAllReachMin(point point) {
        List<point> allMin=new ArrayList<>();
        if(point.getInIncluded()==0){
            point.setInIncluded(1);
            List<point> list= getReachMin(point);
            allMin.addAll(list);
            for(point p:list){
                getAllReachMin(p);
            }
        }
        else{
            return null;
        }
return allMin;

    }

//    //得到一个点的聚类
//    public static Set<point> getCluster(point point,Integer clusterNum) {
//        point.setCluster(clusterNum);
//        System.out.println("这个点是"+point);
//        System.out.println(getAllReachMin(point));
//        Integer isInclude=point.getInIncluded();
//        Integer isMin=point.getIsMin();
//        if(isInclude==1){
//            Set<point> set = new HashSet<>(getReach(point));
//            System.out.println(set);
//            return set;
//        }else{
//           point.setInIncluded(1);
//            //得到这个点的直接可到达的点
//            List<point> canReach=getReach(point);
//            //将可到达的点全部加入簇中
//            for(point point1:canReach){
//                cluster.add(point1);
//                point1.setCluster(clusterNum);
//            }
//            //得到这个点内的所有核心点所在的位置的数组
//            List<Integer> min=getMin(canReach);
//            for(Integer i:min){
//                point newMin=dataList.get(i);
//                getCluster(newMin,clusterNum);
//            }
//        }
//
//        return cluster;
//    }
    public static void main(String args[]){
        //初始化
        initDataList();
//        System.out.println(dataList);
        //计算簇的数量
        Integer clusterNum=1;
        for(point point:dataList){
            if(point.cluster==0){
                List<point> canReach=getReach(point);
                if(canReach.size()>=minPoints){
                    minPoint.add(point);
                    point.isMin=1;
                }
            }
        }
//        System.out.println(minPoint);
        for(point point:minPoint){
            List<point> list=getAllReachMin(point);
//     System.out.println(list);
     if(list!=null&& !list.isEmpty()){
List<point> cluster=new ArrayList<>();
         for(point p:list){
             List<point> canReach=getReach(p);
             for(point point1:canReach){
                 point1.setCluster(clusterNum);
               cluster.add(point1);
             }

         }
         System.out.println(cluster);
         clusterNum++;
     }


 }

//            if(list!=null){
//                for(point p:list){
//                    List<point> reach=getReach(p);
//                    for(point point1:reach){
//                        point1.setCluster(clusterNum);
//                    }
//                   cluster.addAll(reach);
//                }
//                System.out.println(cluster);
//                clusterNum++;
//            }

    }

    public static void getFileData(){
        try {
            FileInputStream inputStream = new FileInputStream("E:\\DataMining Learning\\dbscan.xls");

            List<ExcelData> fileData = EasyExcel.read(inputStream).head(ExcelData.class).sheet()
                    .headRowNumber(1).doReadSync();
            for (ExcelData excelData : fileData) {
                point point = new point(excelData.getX(), excelData.getY());
                dataList.add(point);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void initDataList(){
        getFileData();
    }
}
