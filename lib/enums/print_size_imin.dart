
/// Enum Print Size
///
/// Used to define print text width
/// 384=58mm,
/// 576=80mm
enum PrintSizeImin {
  mm58(384),
  mm80(576);

  final int value;

  const PrintSizeImin(this.value);
}
