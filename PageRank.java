import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//定义矩阵的单个元素，有分子和分母
class element{
    Integer son;
    Integer mom;

    Double result;

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public element(Integer son, Integer mom, Double result) {
        this.son = son;
        this.mom = mom;
        this.result = result;
    }

    public element(Integer son, Integer mom) {
        this.son = son;
        this.mom = mom;
    }


    public Integer getSon() {
        return son;
    }

    public void setSon(Integer son) {
        this.son = son;
    }

    public Integer getMom() {
        return mom;
    }

    public void setMom(Integer mom) {
        this.mom = mom;
    }

    public element() {
    }

    @Override
    public String toString() {
        return "element{" +
                "son=" + son +
                ", mom=" + mom +
                ", result=" + result +
                '}';
    }
}

public class PageRank {
    //定义矩阵
    public static List<element[]> dataList = new ArrayList<>();
    //定义矩阵中的每一项
    public static element s00,s01,s02,s03,s10,s11,s12,s13,s20,s21,s22,s23,s30,s31,s32,s33;
    //得到转移矩阵
    public static Double  teleport[][]={{0.0,0.0,0.0,0.0},{0.0,0.0,0.0,0.0},{0.0,0.0,0.0,0.0},{0.0,0.0,0.0,0.0}}
;
    public static Double ibselo=0.1;

    public static void main(String[] args) {
        init();
        element voScore = new element(1, 4);
        element Score0 = new element(0, 0,0.0);
        element[] V0 = {voScore, voScore, voScore, voScore};
        element[] pageRank = V0;
        //单位矩阵x1/4
        List<element[]> R0 = new ArrayList<>();
        R0.add(V0);
        R0.add(V0);
        R0.add(V0);
        R0.add(V0);
//得到转移概率矩阵
for(int i=0;i< R0.size();i++){
    element[] e1= R0.get(i);
    element[] e2=dataList.get(i);
    element[] e3={Score0,Score0,Score0,Score0};
    for(int j=0;j<e1.length;j++){
        if(e2[j].getSon()==0){
            e3[j].setResult(e1[j].getSon()*0.15/e1[j].getMom());
        }else{
            e3[j].setResult(e1[j].getSon()*0.15/e1[j].getMom()+e2[j].getSon()*0.85/e2[j].getMom());
        }
teleport[i][j]=e3[j].getResult();
    }
}
//R0
        Double[] Rn={1.0,1.0,1.0,1.0};
////输出转移矩阵
//for(int i=0;i< teleport.length;i++){
//    for(int j=0;j< teleport.length;j++){
//        System.out.println(teleport[i][j]+" ");
//    }
//    System.out.println();
//}

//        //显示原矩阵
//for(int i=0;i<dataList.size();i++){
//    element[] e=dataList.get(i);
//    for(int j=0;j<e.length;j++){
//        System.out.print(e[j]+" ");
//    }
//    System.out.println(" ");
//}
        //循环进行PageRank
        while(hasNext(Rn)){
            Rn=getPageRank(teleport,Rn);
            System.out.println(Arrays.toString(Rn));
        }
        System.out.println(Arrays.toString(Rn));
    }
    //计算是否继续迭代
    public static boolean hasNext(Double[] Rn){
        Double[] result=getPageRank(teleport,Rn);
        Double ibselon=0.0;
        for(int i=0;i<result.length;i++){
            ibselon+=Math.abs(result[i]-Rn[i]);
        }
return ibselon>ibselo;
    }


    //计算PageRank
public static Double[] getPageRank(Double[][] M,Double []Rn){
        Double[] result={0.0,0.0,0.0,0.0};
        for(int i=0;i<Rn.length;i++){
            double resultI=0.0;
           for(int j=0;j<M.length;j++){
               resultI+=M[i][j]*Rn[j];
           }
            result[i] =resultI;  }
        return result;

    }


    public static void init(){
        s00 = new element(0, 0);
        s01 = new element(1, 2);
        s02 = new element(0, 0);
        s03 = new element(1, 2);
        s10 = new element(1, 3);
        s11 = new element(0, 0);
        s12 = new element(0, 0);
        s13 = new element(1, 2);
        s20 = new element(1, 3);
        s21 = new element(1, 2);
        s22 = new element(0, 0);
        s23 = new element(0, 0);
        s30 = new element(1, 3);
        s31 = new element(0, 0);
        s32 = new element(1, 1);
        s33 = new element(0, 0);
        element[] s0 = {s00, s01, s02, s03};
        element[] s1 = {s10, s11, s12, s13};
        element[] s2 = {s20, s21, s22, s23};
        element[] s3 = {s30, s31, s32, s33};
        dataList.add(s0);
        dataList.add(s1);
        dataList.add(s2);
        dataList.add(s3);
    }
}
