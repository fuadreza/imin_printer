
import 'dart:typed_data';

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

  Future<void> printText(String text) async {
    IminPrinterPlatform.instance.printText(text);
  }

  Future<void> print2ColumnsText(List<String> listText) async {
    IminPrinterPlatform.instance.print2ColumnsText(listText);
  }
}
