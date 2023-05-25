

import 'package:flutter/services.dart';
import 'package:imin_printer/enums/print_size_imin.dart';
import 'package:imin_printer/print_style.dart';

import 'imin_printer_platform_interface.dart';

class IminPrinter {
  Future<String?> getPlatformVersion() {
    return IminPrinterPlatform.instance.getPlatformVersion();
  }

  //#region PRINTER

  /// Initialize Imin Printer
  ///
  /// Option to Set paper size with [printSizeImin] with
  /// the default value is for 58mm paper.
  Future<String> initPrinter({PrintSizeImin printSizeImin = PrintSizeImin.mm58}) async {
    try {
      await IminPrinterPlatform.instance.initPrinter(printSizeImin);
      return 'Success';
    } on MissingPluginException catch (error) {
      return 'Error | ${error.message}';
    }
  }

  /// Change Print Size Imin Printer
  ///
  /// Set print size with value :
  /// the default value is for 58mm paper and 80mm
  Future<void> setPrintSize(PrintSizeImin printSizeImin) async {
    IminPrinterPlatform.instance.setPrintSize(printSizeImin);
  }

  /// Get Status
  ///
  /// Return [String] printer status. Different printer
  /// gives different status code.
  /// Please refer to this site:
  /// https://oss-sg.imin.sg/docs/en/PrinterSDK.html
  Future<String> getStatus() async {
    return IminPrinterPlatform.instance.getStatus();
  }

  /// Get Model Name
  ///
  /// Return [String] printer model name.
  Future<String> getModelName() async {
    return IminPrinterPlatform.instance.getModelName();
  }

  /// Print raw bytes
  ///
  /// Accept [bytes] with type [Uint8List] and send
  /// that raw bytes to printer.
  Future<void> printBytes(Uint8List bytes) async {
    IminPrinterPlatform.instance.printBytes(bytes);
  }

  /// Print Text
  ///
  /// Add custom style using [PrintStyle] with values.
  /// like alignment, text size, and font style.
  Future<void> printText(String text, {PrintStyle printStyle = const PrintStyle()}) async {
    IminPrinterPlatform.instance.printText(text, printStyle);
  }

  /// Print 2 Columns Text
  ///
  /// Accept [listText] with only 2 text [String].
  /// Option to add custom style for text size.
  Future<void> print2ColumnsText(List<String> listText, {PrintStyle printStyle = const PrintStyle()}) async {
    IminPrinterPlatform.instance.print2ColumnsText(listText, printStyle);
  }

  /// Print Image Bitmap
  ///
  /// Accept [Uint8List] bitmap wrap in [bytes].
  Future<void> printBitmap(Uint8List bytes) async {
    IminPrinterPlatform.instance.printBitmap(bytes);
  }

  /// Print Image Base 64
  ///
  /// Accept [String] bitmap based on base64 format [base64].
  Future<void> printBitmapBase64(String base64) async {
    IminPrinterPlatform.instance.printBitmapBase64(base64);
  }

  /// Set style for printer
  ///
  /// Set default style for printing using [PrintStyle].
  Future<void> setStyle(PrintStyle printStyle) async {
    IminPrinterPlatform.instance.setStyle(printStyle);
  }

  /// Partial cut
  ///
  /// Perform Partial cut paper in printing
  Future<void> partialCut() async {
    IminPrinterPlatform.instance.partialCut();
  }

  /// Full cut
  ///
  /// Perform Full cut paper in printing
  Future<void> fullCut() async {
    IminPrinterPlatform.instance.fullCut();
  }

  //#endregion PRINTER

  //#region LCD MANAGER

  /// Initialize Imin LCD
  ///
  /// Init imin LCD manager for LCD Support.
  /// LCD is different from dual screen.
  Future<String> initLCDManager() async {
    try {
      await IminPrinterPlatform.instance.initLCDManager();
      return 'Success';
    } on MissingPluginException catch (error) {
      return 'Error | ${error.message}';
    }
  }

  //#endregion LCD MANAGER
}
