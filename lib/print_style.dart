import 'package:imin_printer/enums/print_style_align.dart';
import 'package:imin_printer/enums/print_style_font.dart';

class PrintStyle {
  const PrintStyle({
    this.textAlign = PrintStyleAlign.left,
    this.textSize = 19,
    this.fontStyle = PrintStyleFont.regular,
  });

  final PrintStyleAlign textAlign;
  final int textSize;
  final PrintStyleFont fontStyle;
}
