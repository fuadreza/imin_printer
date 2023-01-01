import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:imin_printer/imin_printer_method_channel.dart';

void main() {
  MethodChannelIminPrinter platform = MethodChannelIminPrinter();
  const MethodChannel channel = MethodChannel('imin_printer');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
