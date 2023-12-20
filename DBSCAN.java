import java.util.*;

public class DBSCAN {
    // 定义初始数据集
    public static List<point> dataList = new ArrayList<>();
    // 定义半径ibselo
    public static double e = 1.0;
    // 定义核心对象内的最少数目
    public static int minPoints = 4;
    //存放核心点
    public static List<point> minPoint=new ArrayList<>();
    //需要返回的簇
    public static Set<point> cluster=new HashSet<>();
    static class point{

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

//得到一个点的聚类
public static Set<point> getCluster(point point,Integer clusterNum) {
point.setCluster(clusterNum);
        if(point.getIsMin()==0){
            Set<point> set = new HashSet<>(getReach(point));
            for(point point1:getReach(point)){
                point1.setCluster(clusterNum);
            }
            return set;
        }else{
            point.setIsMin(0);
            //得到这个点的直接可到达的点
            List<point> canReach=getReach(point);
            //将可到达的点全部加入簇中
            for(point point1:canReach){
                cluster.add(point1);
                point1.setCluster(clusterNum);
            }
            //得到这个点内的所有核心点所在的位置的数组
            List<Integer> min=getMin(canReach);
            for(Integer i:min){
                point newMin=dataList.get(i);
                getCluster(newMin,clusterNum);
            }
        }

    return cluster;
}
    public static void main(String args[]){
        //初始化
        initDataList();

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
for(point point:minPoint){
    if(point.getIsMin()==1){
        cluster.clear();
        point.setCluster(clusterNum);
        Set<point> cluster=getCluster(point,clusterNum);
        System.out.println(cluster);
        clusterNum++;
    }
}
    }


    public static void initDataList(){

        point p1 = new point(1.0, 0.0);
        point p2 = new point(4.0, 0.0);
        point p3 = new point(0.0, 1.0);
        point p4 = new point(1.0, 1.0);
        point p5 = new point(2.0, 1.0);
        point p6 = new point(3.0, 1.0);
        point p7 = new point(4.0, 1.0);
        point p8 = new point(5.0, 1.0);
        point p9 = new point(0.0, 2.0);
        point p10 = new point(1.0, 2.0);
        point p11 = new point(4.0, 2.0);
        point p12 = new point(1.0, 3.0);
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
    }
}
