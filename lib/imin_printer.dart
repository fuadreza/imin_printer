
import 'imin_printer_platform_interface.dart';

class IminPrinter {
  Future<String?> getPlatformVersion() {
    return IminPrinterPlatform.instance.getPlatformVersion();
  }

  Future<void> initPrinter() async {
    //TODO IMPLEMENT INIT
    IminPrinterPlatform.instance.initPrinter();
  }

  Future<String> getStatus() async {
    //TODO
    throw UnimplementedError('ERROR');
  }

  Future<void> printBytes(List<int> bytes) async {
    //TODO
    throw UnimplementedError('ERROR');
  }
}
