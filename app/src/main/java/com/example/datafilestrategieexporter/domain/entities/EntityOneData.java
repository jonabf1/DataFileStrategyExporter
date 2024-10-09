
package com.example.datafilestrategieexporter.domain.entities;

import com.example.datafilestrategieexporter.domain.annotations.ExcelColumn;
import com.example.datafilestrategieexporter.domain.annotations.IgnoreIfEmpty;
import com.example.datafilestrategieexporter.domain.annotations.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EntityOneData {

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
    