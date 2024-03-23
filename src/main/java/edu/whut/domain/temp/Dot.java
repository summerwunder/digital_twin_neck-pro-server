package edu.whut.domain.temp;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Dot {
    @ExcelIgnore
    private Long id;
    @ExcelProperty(value="x")
    private Double x;
    @ExcelProperty(value="y")
    private Double y;
    @ExcelProperty(value="z")
    private Double z;
    @ExcelProperty(value="val")
    private Double data;
}