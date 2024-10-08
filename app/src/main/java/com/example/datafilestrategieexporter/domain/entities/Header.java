
package com.example.datafilestrategieexporter.domain.entities;

import com.example.datafilestrategieexporter.domain.annotations.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Header {

    @ExcelColumn(name = "field1")
    private int field1;

    @ExcelColumn(name = "field2")
    private String field2;

    @ExcelColumn(name = "field3")
    private Integer field3;

}
    