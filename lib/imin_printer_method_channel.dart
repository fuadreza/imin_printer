import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:imin_printer/enums/print_size_imin.dart';
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

  //#region PRINTER

  @override
  Future<String?> initPrinter(PrintSizeImin printSizeImin) async {
    try {
      return await methodChannel.invokeMethod<String>('initPrinter', {'printSize': printSizeImin.value});
    } on MissingPluginException catch (_) {
      throw MissingPluginException('No method found for initPrinter() on channel');
    }
  }

  @override
  Future<String?> setPrintSize(PrintSizeImin printSizeImin) async {
    try {
      return await methodChannel.invokeMethod<String>('setPrintSize', {'printSize': printSizeImin.value});
    } on MissingPluginException catch (_) {
      throw MissingPluginException('No method found for setPrintSize() on channel');
    }
  }

  @override
  Future<String> getStatus() async {
    try {
      return await methodChannel.invokeMethod<String>('getStatus') ?? 'invalid';
    } on MissingPluginException catch (_) {
      throw MissingPluginException('No method found for getStatus() on channel');
    }
  }

  @override
  Future<String> getModelName() async {
    try {
      return await methodChannel.invokeMethod<String>('getModelName') ?? 'invalid';
    } on MissingPluginException catch (_) {
      throw MissingPluginException('No method found for getStatus() on channel');
    }
  }

  @override
  Future<String> printBytes(Uint8List bytes) async {
    try {
      return await methodChannel.invokeMethod<String>('printBytes', {'bytes': bytes}) ?? 'invalid';
    } on MissingPluginException catch (_) {
      throw MissingPluginException('No method found for printBytes() on channel');
    }
  }

  @override
  Future<String> printText(String text, PrintStyle printStyle) async {
    try {
      return await methodChannel.invokeMethod<String>('printText', {
            'text': text,
            'textAlign': printStyle.textAlign.value,
            'textSize': printStyle.textSize,
            'fontStyle': printStyle.fontStyle.value,
          }) ??
          'invalid';
    } on MissingPluginException catch (_) {
      throw MissingPluginException('No method found for printText() on channel');
    }
  }

  @override
  Future<String> print2ColumnsText(List<String> listText, PrintStyle printStyle) async {
    try {
      return await methodChannel.invokeMethod<String>('print2ColumnsText', {'texts': listText, 'textAlign': printStyle.textSize}) ?? 'invalid';
    } on MissingPluginException catch (_) {
      throw MissingPluginException('No method found for print2ColumnsText() on channel');
    }
  }

  @override
  Future<String> printBitmap(Uint8List bytes) async {
    try {
      return await methodChannel.invokeMethod<String>('printBitmap', {'bytes': bytes}) ?? 'invalid';
    } on MissingPluginException catch (_) {
      throw MissingPluginException('No method found for printBitmap() on channel');
    }
  }

  @override
  Future<String> printBitmapBase64(String base64) async {
    try {
      return await methodChannel.invokeMethod<String>('printBitmapBase64', {'base64': base64}) ?? 'invalid';
    } on MissingPluginException catch (_) {
      throw MissingPluginException('No method found for printBitmapBase64() on channel');
    }
  }

  @override
  Future<String> setStyle(PrintStyle printStyle) async {
    try {
      return await methodChannel.invokeMethod<String>('setStyle', {
            'textAlign': printStyle.textAlign.value,
            'textSize': printStyle.textSize,
            'fontStyle': printStyle.fontStyle.value,
          }) ??
          'invalid';
    } on MissingPluginException catch (_) {
      throw MissingPluginException('No method found for setStyle() on channel');
    }
  }

  @override
  Future<String> partialCut() async {
    try {
      return await methodChannel.invokeMethod<String>('partialCut') ?? 'invalid';
    } on MissingPluginException catch (_) {
      throw MissingPluginException('No method found for partialCut() on channel');
    }
  }

  @override
  Future<String> fullCut() async {
    try {
      return await methodChannel.invokeMethod<String>('fullCut') ?? 'invalid';
    } on MissingPluginException catch (_) {
      throw MissingPluginException('No method found for fullCut() on channel');
    }
  }

  //#endregion PRINTER

  //#region LCD MANAGER

  @override
  Future<String> initLCDManager() async {
    try {
      return await methodChannel.invokeMethod<String>('initLCDManager') ?? 'invalid';
    } on MissingPluginException catch (_) {
      throw MissingPluginException('No method found for initLCDManager() on channel');
    }
  }

  @override
  Future<String> sendBitmapBase64LCDScreen(String base64) async {
    try {
      return await methodChannel.invokeMethod<String>('sendBitmapBase64LCDScreen', {'base64': base64}) ?? 'invalid';
    } on MissingPluginException catch (_) {
      throw MissingPluginException('No method found for sendBitmapBase64() on channel');
    }
  }

  @override
  Future<String> clearLCDScreen() async {
    try {
      return await methodChannel.invokeMethod<String>('clearLCDScreen') ?? 'invalid';
    } on MissingPluginException catch (_) {
      throw MissingPluginException('No method found for clearLCDScreen() on channel');
    }
  }

//#endregion LCD MANAGER
}
