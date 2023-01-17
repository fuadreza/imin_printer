import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:imin_printer/print_style.dart';

import 'imin_printer_platform_interface.dart';

/// An implementation of [IminPrinterPlatform] that uses method channels.
class MethodChannelIminPrinter extends IminPrinterPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('imin_printer');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<String?> initPrinter() async {
    return await methodChannel.invokeMethod<String>('initPrinter');
  }

  @override
  Future<String> getStatus() async {
    return await methodChannel.invokeMethod<String>('getStatus') ?? 'invalid';
  }

  @override
  Future<String> printBytes(Uint8List bytes) async {
    return await methodChannel.invokeMethod<String>('printBytes', {'bytes': bytes}) ?? 'invalid';
  }

  @override
  Future<String> printText(String text, PrintStyle printStyle) async {
    await setStyle(printStyle);
    await methodChannel.invokeMethod<String>('printText', {'text': text}) ?? 'invalid';
    return await setStyle(const PrintStyle());
  }

  @override
  Future<String> print2ColumnsText(List<String> listText) async {
    return await methodChannel.invokeMethod<String>('print2ColumnsText', {'texts': listText}) ?? 'invalid';
  }

  @override
  Future<String> setStyle(PrintStyle printStyle) async {
    return await methodChannel.invokeMethod<String>('setStyle', {
          'textAlign': printStyle.textAlign.value,
          'textSize': printStyle.textSize,
          'fontStyle': printStyle.fontStyle.value,
        }) ??
        'invalid';
  }
}
