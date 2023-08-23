# Imin Printer

A flutter package to support command on Imin devices

## Support

- [x] Android
- [ ] IOS

## Installation

```bash
flutter pub add imin_printer
```

## What this package do

### Printer Related
Tested on: Imin D1, Imin D4 Pro, Imin M2-202, Imin M2 Pro
- [x] Initialize Printer
- [x] Set print size
- [x] Get Printer Status
- [x] Get model name
- [x] Get brand name
- [x] Print raw bytes (Uint8List)
- [x] Print text
- [x] Print 2 Column text
- [x] Print Bitmap (Uint8List)
- [x] Print Bitmap base64 (String)
- [x] Set Style
- [x] Partial cut
- [x] Full cut

### LCD Display Related
Tested on: Imin D1
- [x] Initialize LCD Manager
- [x] Send bitmap base 64 to LCD Screen
- [x] Clear LCD Screen

### Cash Drawer Related
Tested on: Imin D4 Pro
- [x] Open cash drawer

### You can combine this package with [presentation_display](https://pub.dev/packages/presentation_displays) to use customer display on some Imin device

## Example

### Instance
```dart
IminPrinter iminPrinter = IminPrinter();
```

### Init Printer
Use init before accessing other functionality
```dart
iminPrinter.initPrinter(printSizeImin: PrintSizeImin.mm58);
```

### Print Text
```dart
iminPrinter.printText('Sample Text', printStyle: const PrintStyle(textAlign: PrintStyleAlign.center)); // Print text on Center
```

### Print 2 Column Text
```dart
iminPrinter.print2ColumnsText(['Left Text', 'Right Text']);
```
