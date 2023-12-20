package org.example;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExcelData {
    @ExcelProperty(value = "横坐标")
    private double x;

    @ExcelProperty(value = "纵坐标")
    private double y;
}
