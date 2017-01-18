package com.github.EJBM_Einstein.StampDOI;

import java.awt.*;
import java.io.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.*;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.PDPageContentStream.*;
import org.apache.pdfbox.util.*;

public class StampDOI {
    private static final String DOI_PREFIX = "10.23861.EJBM/";

    // --- Lower-right, bold, dark gray ---
    /*private static final PDFont DOI_FONT = PDType1Font.HELVETICA_BOLD;
    private static final float DOI_FONT_SIZE = 8.0f;
    private static final Color DOI_FONT_COLOR = new Color(60, 60, 60);

    private static final float X_POSITION = 551.0f;
    private static final float Y_POSITION = 11.0f;*/

    // --- Lower-right, normal, black ---
    private static final PDFont DOI_FONT = PDType1Font.HELVETICA;
    private static final float DOI_FONT_SIZE = 8.0f;
    private static final Color DOI_FONT_COLOR = new Color(48, 48, 48);

    private static final float X_POSITION = 551.0f;
    private static final float Y_POSITION = 11.0f;

    public static void main(String[] args) {
        if (args.length != 3) {
            printUsage();
            System.exit(1);
        }

        File pdfOut = new File(args[0]);
        File pdfIn = new File(args[1]);
        String doi = args[2];

        stamp(pdfOut, pdfIn, doi);
    }

    private static void printUsage() {
        System.err.println( "usage: java -jar StampDOI.jar <output_PDF> <input_PDF> <DOI>" );
    }

    private static void stamp(File pdfOut, File pdfIn, String doiText) {
        doiText = DOI_PREFIX + doiText;
        PDDocument document = null;

        try {
            document = PDDocument.load(pdfIn);

            PDPage page = document.getPage(0);
            PDRectangle pageSize = page.getMediaBox();
            float stringWidth = DOI_FONT.getStringWidth(doiText) * DOI_FONT_SIZE / 1000f;

            PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true, true);
            contentStream.beginText();
            contentStream.setFont(DOI_FONT, DOI_FONT_SIZE);
            contentStream.setNonStrokingColor(DOI_FONT_COLOR);
            contentStream.setTextMatrix(Matrix.getTranslateInstance(X_POSITION - stringWidth, Y_POSITION));

            contentStream.showText(doiText);
            contentStream.endText();
            contentStream.close();

            document.save(pdfOut);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
    }
}