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
    @ExcelProperty(index = 1)
    private Double x;
    @ExcelProperty(index = 2)
    private Double y;
    @ExcelProperty(index = 3)
    private Double z;
    @ExcelProperty(index = 4)
    private Double data;
}