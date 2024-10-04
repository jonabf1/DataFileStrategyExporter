
package com.example.writerdatatofile.core.interfaces;

import com.example.writerdatatofile.application.usecases.ExportBuilder;

import java.io.IOException;

public interface ExportStrategy {
    void export(ExportBuilder command) throws IOException;
}
