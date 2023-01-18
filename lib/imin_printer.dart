
import 'dart:typed_data';

import 'package:imin_printer/print_style.dart';

import 'imin_printer_platform_interface.dart';

class IminPrinter {
  Future<String?> getPlatformVersion() {
    return IminPrinterPlatform.instance.getPlatformVersion();
  }

  Future<void> initPrinter() async {
    IminPrinterPlatform.instance.initPrinter();
  }

  Future<String> getStatus() async {
    return IminPrinterPlatform.instance.getStatus();
  }

  Future<void> printBytes(Uint8List bytes) async {
    IminPrinterPlatform.instance.printBytes(bytes);
  }

  Future<void> printText(String text, {PrintStyle printStyle = const PrintStyle()}) async {
    IminPrinterPlatform.instance.printText(text, printStyle);
  }

  Future<void> print2ColumnsText(List<String> listText, {PrintStyle printStyle = const PrintStyle()}) async {
    IminPrinterPlatform.instance.print2ColumnsText(listText, printStyle);
  }

  Future<void> setStyle(PrintStyle printStyle) async {
    IminPrinterPlatform.instance.setStyle(printStyle);
  }
}
