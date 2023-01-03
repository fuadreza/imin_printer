import 'package:flutter_test/flutter_test.dart';
import 'package:imin_printer/imin_printer.dart';
import 'package:imin_printer/imin_printer_platform_interface.dart';
import 'package:imin_printer/imin_printer_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockIminPrinterPlatform
    with MockPlatformInterfaceMixin
    implements IminPrinterPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  Future<String?> initPrinter() => Future.value('init');

  @override
  Future<String> getStatus() => Future.value('active');
}

void main() {
  final IminPrinterPlatform initialPlatform = IminPrinterPlatform.instance;

  test('$MethodChannelIminPrinter is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelIminPrinter>());
  });

  test('getPlatformVersion', () async {
    IminPrinter iminPrinterPlugin = IminPrinter();
    MockIminPrinterPlatform fakePlatform = MockIminPrinterPlatform();
    IminPrinterPlatform.instance = fakePlatform;

    expect(await iminPrinterPlugin.getPlatformVersion(), '42');
  });
}
