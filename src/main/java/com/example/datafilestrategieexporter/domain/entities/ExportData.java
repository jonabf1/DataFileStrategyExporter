
package com.example.writerdatatofile.domain.entities;

import com.example.writerdatatofile.domain.annotations.ExcelColumn;
import com.example.writerdatatofile.domain.annotations.IgnoreIfEmpty;
import com.example.writerdatatofile.domain.annotations.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExportData {

    @ExcelColumn(name = "ID")
    @Order(1)
    private int id;

    @ExcelColumn(name = "Full Name")
    @Order(2)
    private String name;

    @ExcelColumn(name = "Age")
    @Order(3)
    private Integer age;

    @ExcelColumn(name = "Email Address")
    @Order(4)
    @IgnoreIfEmpty
    private String email;

}
    