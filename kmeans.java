
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public  class kmeans {
    static class point{

        private Integer cluster=0;
        private Double x;
        private Double y;
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            point point = (point) obj;
            return Objects.equals(x, point.x) &&
                    Objects.equals(y, point.y);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
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

        public point() {
        }



    }
    public static double calculateDistance(point p1, point p2){
        double x1 = p1.getX() - p2.getX();
        double y1= p1.getY() - p2.getY();
        double temp = x1 * x1 + y1 * y1;
        return Math.sqrt(temp);
    }
    //定义初始数据集
    public static List<point> dataList = new ArrayList<>();
    //定义簇的数目
    public static Integer k = 3;
    //定义三个簇的均值点
    public static point p1;
    public static point p2;
    public static point p3;

    public static void main(String[] args) {
        //初始化数据集和初始簇
        initDataList();
        Random random = new Random();
        Set<Integer> uniqueRandomNumbers = new HashSet<>();
        // 生成不重复的随机点
        while (uniqueRandomNumbers.size() < k) {
            int randomNumber = getRandomInRange(random, 0, dataList.size()-1);
            uniqueRandomNumbers.add(randomNumber);
        }
        Integer[] uniqueRandomArray = uniqueRandomNumbers.toArray(new Integer[0]);
        point Apoint1=dataList.get(uniqueRandomArray[0]);
        point Apoint2=dataList.get(uniqueRandomArray[1]);
        point Apoint3=dataList.get(uniqueRandomArray[2]);
        System.out.println("Random point is : "+Apoint1+" "+Apoint2+" "+Apoint3);
        //初始化均值点
        p1=Apoint1;
        p2=Apoint2;
        p3=Apoint3;
        p1.setCluster(0);
        p3.setCluster(2);
        p2.setCluster(1);
        //while(！聚类不再改变）
        //遍历数据集并划分簇
        while(true){
//            !(equal(Average(0),p1)&&equal(Average(1),p2)&&equal(Average(0),p3))
            //分簇
            for (int i=0;i<dataList.size();i++){
                Double minDis=Double.MAX_VALUE;
                int minIndex=-1;
                point[] compare={p1,p2,p3};
                //找到最小距离的点
                for(int j=0;j<compare.length;j++){
                    Double dis=calculateDistance(dataList.get(i),compare[j]);
                    if(dis<minDis){
                        minDis=dis;
                        minIndex=j;
                    }
                }
                //该点被分到距离点距离最小的簇中
                System.out.println(dataList.get(i)+" most near is "+compare[minIndex]);
                dataList.get(i).setCluster(compare[minIndex].getCluster());
            }
            System.out.println("average is :"+p1+" "+p2+" "+p3);
            point a1=Average(0);
            point a2=Average(1);
            point a3=Average(2);
            if(!(p1.equals(a1)&&p2.equals(a2)&&p3.equals(a3))){
                p1=Average(0);
                p1.setCluster(0);
                p2=Average(1);
                p2.setCluster(1);
                p3=Average(2);
                p3.setCluster(2);
                System.out.println("new average is"+Average(0)+" "+Average(1)+" "+Average(2));

                continue;
            }
            break;

        }

for(int i=0;i<3;i++){
    System.out.println("cluster "+i+":");
    for(int j=0;j< dataList.size()-1;j++){
        if(dataList.get(j).getCluster()==i){
            System.out.print(dataList.get(j));
        }
    }
    System.out.println();
}
        System.out.println("the final average is "+p1+" "+p2+" "+p3+" ");
//        c1.setElements();
    }
    //求一个簇的均值点
    private static point Average(int targetCluster) {
        List<point> clusterPoints = dataList.stream()
                .filter(point -> point.cluster.equals(targetCluster))
                .collect(Collectors.toList());
        if (!clusterPoints.isEmpty()) {
            double averageX = clusterPoints.stream().mapToDouble(point::getX).average().orElse(0.0);
            double averageY = clusterPoints.stream().mapToDouble(point::getY).average().orElse(0.0);
            return new point(targetCluster, averageX, averageY);
        }
        return null;
    }
//
//    private static point Average(int cluster) {
//        Double xAll=0.0;
//        Double yAll=0.0;
//        int x=0;int y=0;
//        int flag=0;
//        for(int i=0;i<dataList.size();i++){
//            //选中的点
//            point tem=dataList.get(i);
//            if (tem.getCluster()==cluster){
//                flag=1;
//                xAll+=tem.getX();x++;
//                yAll+=tem.getY();y++;
//            }
//        }
//        if(flag==0){
//            x=1;y=1;
//        }
//        point p=new point(xAll/x,yAll/y);
//        return p;
//    }

    //指定范围的随机整数
    private static int getRandomInRange(Random random, int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    public static void initDataList(){
        point p1 = new point(1.0, 2.0);
        point p2 = new point(2.0, 1.0);
        point p3 = new point(2.0, 4.0);
        point p4 = new point(4.0, 3.0);
        point p5 = new point(5.0, 8.0);
        point p6 = new point(6.0, 7.0);
        point p7 = new point(6.0, 9.0);
        point p8 = new point(7.0, 9.0);
        point p9 = new point(9.0, 5.0);
        point p10 = new point(1.0, 12.0);
        point p11 = new point(3.0, 12.0);
        point p12 = new point(5.0, 12.0);
        point p13 = new point(3.0, 3.0);
        dataList.add(p1);
        dataList.add(p2);
        dataList.add(p3);
        dataList.add(p4);
        dataList.add(p5);
        dataList.add(p6);
        dataList.add(p7);
        dataList.add(p8);
        dataList.add(p9);
        dataList.add(p10);
        dataList.add(p11);
        dataList.add(p12);
        dataList.add(p13);
    }
}
