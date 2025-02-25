package com.sahil.service.highlight.util;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.kernel.pdf.layer.PdfLayer;
import com.sahil.service.highlight.model.Diff;
import com.sahil.service.highlight.model.DiffList;
import com.sahil.service.highlight.model.Operation;

public class TextHighlighter extends PDFTextStripper {

  public StringBuilder sb;
  private List<Diff> diffs;
  private Operation operation;
  private Operation OPERATION_EQUAL = Operation.EQUAL;
  private PDDocument document;
  private Highlight highlighter;
  private int diffIndex;
  private int diffTextIndex;
  private int remainingDiffLength;
  private Diff diff;
  private boolean highlightSpaces;
  private boolean jumpToNextLine = false;

  public TextHighlighter(String inputFilename, String outputFilename, DiffList diffList,
      Operation operation, boolean highlightSpaces) throws IOException {
    this.diffs = diffList.getDiffs();
    this.operation = operation;
    this.highlighter = new Highlight(inputFilename, outputFilename);
    this.highlightSpaces = highlightSpaces;

    try {
      this.document = PDDocument.load(new File(inputFilename));
      this.setSortByPosition(true);
      this.setStartPage(1);
      this.setEndPage(this.document.getNumberOfPages());
      Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
      this.writeText(document, dummy);
    } finally {
      if (this.document != null) {
        this.document.close();
      }
    }

  }

  @Override
  protected void writeString(String line, List<TextPosition> textPositions) throws IOException {

    for (TextPosition textPosition : textPositions) {
      System.out.print(textPosition);
    }
    System.out.println();

    int textPositionIndex = 0;
    while (textPositionIndex < textPositions.size() && this.diffIndex < this.diffs.size()) {

      this.diff = this.diffs.get(this.diffIndex);
      while (this.diff.operation != this.operation && this.diff.operation != this.OPERATION_EQUAL
          && this.diffIndex < this.diffs.size()) {
        this.diffIndex++;
        this.diff = this.diffs.get(this.diffIndex);
      }

      String diffText = this.diff.text.replaceAll("\n", "");
      String remainingDiffText = diffText.substring(this.diffTextIndex);
      this.remainingDiffLength = remainingDiffText.length();
      List<TextPosition> remainingTextPositions =
          textPositions.subList(textPositionIndex, textPositions.size());
      int remainingTextPositionsLength = remainingTextPositions.size();

      this.jumpToNextLine = false;

      TextPosition firstTextPosition = remainingTextPositions.get(0);
      float startX = firstTextPosition.getXDirAdj();
      float startY = firstTextPosition.getYDirAdj();
      float adjustedStartY = getCurrentPage().getMediaBox().getHeight() - startY;
      TextPosition lastTextPosition;
      boolean lineEnd = false;

      if (this.remainingDiffLength >= remainingTextPositionsLength) {
        this.diffTextIndex += remainingTextPositionsLength;

        this.jumpToNextLine = true;
        lastTextPosition = remainingTextPositions.get(remainingTextPositionsLength - 1);

        lineEnd = true;
      } else {
        lastTextPosition = remainingTextPositions.get(this.remainingDiffLength);
        textPositionIndex += this.remainingDiffLength;

        this.diffIndex++;
        this.diffTextIndex = 0;
      }

      float endX = lastTextPosition.getXDirAdj();
      if (lineEnd) {
        endX += lastTextPosition.getWidth();
      }

      float rectangleWidth = endX - startX;
      float rectangleHeight = lastTextPosition.getHeight();

      int currentPageNumber = getCurrentPageNo();
      switch (this.diff.operation) {
        case INSERT:

          if (!diffText.equals(" ") || highlightSpaces) {
            this.highlighter.highlight(currentPageNumber, startX,
                (float) (adjustedStartY - rectangleHeight * 0.4), rectangleWidth,
                (float) (rectangleHeight * 1.8), Color.GREEN);
          }

          break;

        case DELETE:

          if (!diffText.equals(" ") || highlightSpaces) {
            this.highlighter.highlight(currentPageNumber, startX,
                (float) (adjustedStartY - rectangleHeight * 0.4), rectangleWidth,
                (float) (rectangleHeight * 1.8), Color.RED);
          }

          break;

        case EQUAL:

          break;
      }
      if (this.jumpToNextLine) {
        break;
      }
    }
  }

  public void close() {
    this.highlighter.close();
  }

  private static class Highlight {

    private PdfLayer pdflayer;
    private PdfDocument pdfDoc;

    public Highlight(String inputFilename, String outputFilename) {

      try {
        this.pdfDoc = new PdfDocument(new PdfReader(inputFilename), new PdfWriter(outputFilename));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    public void highlight(int pageNumber, float x, float y, float width, float height,
        java.awt.Color color) {

      try {
        if (pdflayer == null) {
          pdflayer = new PdfLayer("main layer", pdfDoc);
          pdflayer.setOn(true);
        }
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getPage(pageNumber));
        canvas.beginLayer(pdflayer);

        com.itextpdf.kernel.colors.Color clr = ColorConstants.GREEN;
        if (color == java.awt.Color.RED) {
          clr = ColorConstants.RED;
        }

        canvas.saveState().setFillColor(clr).setExtGState(new PdfExtGState().setFillOpacity(0.3f))
            .rectangle(x, y, width, height).fill();
        canvas.endLayer();
        canvas.release();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    public void close() {
      if (pdfDoc != null) {
        pdfDoc.close();
      }
    }
  }
}
