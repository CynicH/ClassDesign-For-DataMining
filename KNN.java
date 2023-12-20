import java.util.*;

class Student{
    String name;
    Integer height;
    String level;


    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", height=" + height +
                ", level='" + level + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Student(String name, Integer height) {
        this.name = name;
        this.height = height;
    }

    public Student() {
    }

    public Student(String name, Integer height, String level) {
        this.name = name;
        this.height = height;
        this.level = level;
    }
}
public class KNN {
    //k的大小
    public static Integer k=5;
    //数据集
    public static List<Student> dataList = new ArrayList<>();
    //邻居集合
    public static List<Student> neighbour=new ArrayList<>();
    public static void main(String[] args) {
        initData();
        Student s0 = new Student("易昌", 174);
//初始化邻居集合
        for(int i=0;i<k;i++){
            neighbour.add(dataList.get(i));
        }
        //循环knn算法
        for(int i=k;i<dataList.size();i++){
            //最远的学生
            Student far=far(s0,neighbour);

            if (heightShort(s0,far)>heightShort(s0,dataList.get(i))){
//                System.out.println(dataList.get(i)+" 替换 "+far);
                neighbour.remove(far);
                neighbour.add(dataList.get(i));
            }
        }
        System.out.println(neighbour);
        //得到knn结果
        Map<String, Integer> levelFrequency = new HashMap<>();
        for (Student student : neighbour) {
            levelFrequency.put(student.level, levelFrequency.getOrDefault(student.level, 0) + 1);
        }

        // 找到出现次数最多的 level
        String mostFrequentLevel = Collections.max(levelFrequency.entrySet(), Map.Entry.comparingByValue()).getKey();
        System.out.println("knn算法得出" +s0+"应该被归类为"+ mostFrequentLevel);
//        Map<String,Integer> knnResult=new HashMap<>();
//        for(int i=0;i< neighbour.size();i++){
//            String lev=neighbour.get(i).getLevel();
//            if(knnResult.containsKey(lev)){
//            }
//        }
    }
    //返回邻居中距离最远的那个
    public static Student far(Student s1,List<Student> neighbours){
      Student farNeighbour=neighbours.get(0);
      Integer maxDistance=0;
        for(int i=0;i<neighbours.size();i++){
             Integer distance=Math.abs(neighbours.get(i).getHeight()-s1.getHeight());
             if(distance>maxDistance){
                 maxDistance=distance;
                 farNeighbour=neighbours.get(i);
             }
        }
        return farNeighbour;
    }
    //计算学生之间身高差
    public static Integer heightShort(Student s1, Student s2){
        return Math.abs(s1.getHeight()-s2.getHeight());
    }
    public static void initData(){
        Student s1 = new Student("李丽", 150, "矮");
        Student s2 = new Student("吉米", 192, "高");
        Student s3 = new Student("马大华", 170, "中等");
        Student s4 = new Student("王晓华", 173, "中等");
        Student s5 = new Student("刘敏", 160, "矮");
        Student s6 = new Student("张强", 175, "中等");
        Student s7 = new Student("李秦", 160, "矮");
        Student s8 = new Student("王壮", 190, "高");
        Student s9 = new Student("刘冰", 168, "中等");
        Student s10 = new Student("张喆", 178, "中等");
        Student s11 = new Student("杨毅", 170, "中等");
        Student s12 = new Student("徐田", 168, "中等");
        Student s13 = new Student("高杰", 165, "矮");
        Student s14 = new Student("张晓", 178, "中等");

        dataList.add(s1);
        dataList.add(s2);
        dataList.add(s3);
        dataList.add(s4);
        dataList.add(s5);
        dataList.add(s6);
        dataList.add(s7);
        dataList.add(s8);
        dataList.add(s9);
        dataList.add(s10);
        dataList.add(s11);
        dataList.add(s12);
        dataList.add(s13);
        dataList.add(s14);
    }
}
