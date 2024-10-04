
package com.example.writerdatatofile.domain.entities;

import com.example.writerdatatofile.annotation.ExcelColumn;
import com.example.writerdatatofile.annotation.ExcelOrder;
import com.example.writerdatatofile.annotation.IgnoreIfEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExportData {

    @ExcelColumn(name = "ID")
    @ExcelOrder(1)
    private int id;

    @ExcelColumn(name = "Full Name")
    @ExcelOrder(2)
    private String name;

    @ExcelColumn(name = "Age")
    @ExcelOrder(3)
    private Integer age;

    @ExcelColumn(name = "Email Address")
    @ExcelOrder(4)
    @IgnoreIfEmpty
    private String email;

}
    