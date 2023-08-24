package io.github.fuadreza.imin_printer

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.imin.image.ILcdManager
import com.imin.library.IminSDKManager
import com.imin.library.SystemPropManager
import com.imin.printerlib.IminPrintUtils
import com.imin.printerlib.IminPrintUtils.PrintConnectType
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.github.fuadreza.imin_printer.extensions.base64ToBitmap


/** IminPrinterPlugin */
class IminPrinterPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    val flagInitLCDManager: Int = 1
    val flagWakeUpLCDManager: Int = 2
    val flagHibernateLCDManager: Int = 3
    val flagClearScreenLCDManager: Int = 4

    private lateinit var channel: MethodChannel
    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var instance: IminPrintUtils
    private lateinit var instanceLcdManager: ILcdManager

    private var connectType = PrintConnectType.USB

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "imin_printer")
        context = flutterPluginBinding.applicationContext
        channel.setMethodCallHandler(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        val arguments: Map<String, Any>? = call.arguments()
        if (call.method == "getPlatformVersion") {
            result.success("Android ${Build.VERSION.RELEASE}")
        } else if (call.method == "initPrinter") {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity, arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
                    ), 0
                )
            }

            val deviceModel = SystemPropManager.getModel()
            val printSize = arguments?.get("printSize") as Int?
            connectType = if (deviceModel.contains("M2-203") || deviceModel.contains("M2-202") || deviceModel.contains("M2-Pro")) {
                PrintConnectType.SPI
            } else {
                PrintConnectType.USB
            }

            try {
                instance.initPrinter(connectType)
            } catch (e: Exception) {
                val newConnectType = if (connectType == PrintConnectType.SPI) {
                    PrintConnectType.USB
                } else {
                    PrintConnectType.SPI
                }
                instance.initPrinter(newConnectType)
            } finally {
                setDefaultStyle(printSize)
                result.success("init")
            }
        } else if (call.method == "initLCDManager") {
            try {
                instanceLcdManager = ILcdManager.getInstance(context)
                instanceLcdManager.sendLCDCommand(flagInitLCDManager)
            } catch (e: Exception) {
            } finally {
                result.success("initLCDManager")
            }
        } else if (call.method == "getStatus") {
            val status: Int = instance.getPrinterStatus(connectType)
            result.success(String.format("%d", status))
        } else if (call.method == "getBrandName") {
            val deviceBrand = SystemPropManager.getBrand()
            result.success(deviceBrand)
        } else if (call.method == "getModelName") {
            val deviceModel = SystemPropManager.getModel()
            result.success(deviceModel)
        } else if (call.method == "setPrintSize") {
            val printSize = arguments?.get("printSize") as Int?
            instance.setTextWidth(printSize ?: 384)
            result.success("success change size to $printSize")
        } else if (call.method == "printBytes") {
            if (arguments == null) return
            val bytes = arguments["bytes"] as ByteArray?
            if (bytes != null) {
                instance.sendRAWData(bytes)
                result.success(bytes)
            } else {
                result.error("invalid_argument", "argument 'bytes' not found", null)
            }
        } else if (call.method == "printText") {
            if (arguments == null) return
            val text = arguments["text"] as String?
            val textAlign = arguments["textAlign"] as Int?
            val textSize = arguments["textSize"] as Int?
            val fontStyle = arguments["fontStyle"] as Int?
            instance.setAlignment(textAlign ?: 0)
            instance.setTextSize(textSize ?: 19)
            instance.setTextStyle(fontStyle ?: 0)
            if (text != null) {
                instance.printText(text + "\n")
                result.success(text)
            } else {
                result.error("invalid_argument", "argument 'text' not found", null)
            }
        } else if (call.method == "print2ColumnsText") {
            if (arguments == null) return
            val arrayText = arguments["texts"] as ArrayList<*>?
            val textSize = arguments["textSize"] as Int?
            if (arrayText != null) {
                val listText: MutableList<String> = mutableListOf<String>()
                arrayText.forEach { item ->
                    item as String?
                    listText.add(item)
                }
                instance.printColumnsText(listText.toTypedArray(), intArrayOf(1, 1), intArrayOf(0, 2), intArrayOf(textSize ?: 19, textSize ?: 19))
                result.success(arrayText)
            } else {
                result.error("invalid_argument", "argument 'text' not found", null)
            }
        } else if (call.method == "setStyle") {
            if (arguments == null) return
            val textAlign = arguments["textAlign"] as Int?
            val textSize = arguments["textSize"] as Int?
            val fontStyle = arguments["fontStyle"] as Int?
            instance.setAlignment(textAlign ?: 0)
            instance.setTextSize(textSize ?: 19)
            instance.setTextStyle(fontStyle ?: 0)
            result.success("success")
        } else if (call.method == "printBitmap") {
            if (arguments == null) return
            var bytes = arguments["bytes"] as ByteArray?
            if (bytes != null) {
                var bitmap: Bitmap? = convertByteArrayToBitmap(bytes)
                instance.printSingleBitmap(bitmap)
                bitmap = null
                bytes = null
                result.success("success")
            } else {
                result.error("invalid_argument", "argument 'bytes' not found", null)
            }
        } else if (call.method == "printBitmapBase64") {
            if (arguments == null) return
            var stringBase64 = arguments["base64"] as String?
            if (stringBase64 != null) {
                var bitmap: Bitmap? = stringBase64.base64ToBitmap()
                instance.printSingleBitmap(bitmap)
                bitmap = null
                stringBase64 = null
                result.success("success")
            } else {
                result.error("invalid_argument", "argument 'bytes' not found", null)
            }
        } else if (call.method == "fullCut") {
            instance.fullCut()
            result.success("success")
        } else if (call.method == "partialCut") {
            instance.partialCut()
            result.success("success")
        } else if (call.method == "sendBitmapBase64LCDScreen") {
            if (arguments == null) return
            var stringBase64 = arguments["base64"] as String?
            if (stringBase64 != null) {
                var bitmap: Bitmap? = stringBase64.base64ToBitmap()
                instanceLcdManager.sendLCDCommand(flagClearScreenLCDManager)
                instanceLcdManager.sendLCDBitmap(bitmap)
                bitmap = null
                stringBase64 = null
                result.success("success")
            } else {
                result.error("invalid_argument", "argument 'bytes' not found", null)
            }
        } else if (call.method == "clearLCDScreen") {
            try {
                instanceLcdManager.sendLCDCommand(flagClearScreenLCDManager)
            } catch (e: Exception) {
            } finally {
                result.success("initLCDManager")
            }
        } else if (call.method == "openCashDrawer") {
            IminSDKManager.opencashBox()
        } else if (call.method == "printQR") {
            if (arguments == null) return
            val qrStr = arguments["data"] as String?
            val size = arguments["size"] as Int?
            instance.setQrCodeSize(size);
            instance.printQrCode(qrStr, 1)
        } else {
            instance.partialCut()
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
        instance = IminPrintUtils.getInstance(activity)
    }

    override fun onDetachedFromActivityForConfigChanges() {
        //TODO("Not yet implemented")
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        //TODO("Not yet implemented")
    }

    override fun onDetachedFromActivity() {
        //TODO("Not yet implemented")
    }

    private fun convertByteArrayToBitmap(bytes: ByteArray): Bitmap {
        val options = BitmapFactory.Options()
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
    }

    private fun setDefaultStyle(printSize: Int?) {
        instance.setAlignment(0)
        instance.setTextSize(19)
        instance.setTextTypeface(Typeface.MONOSPACE);
        instance.setTextStyle(Typeface.NORMAL)
        instance.setTextWidth(printSize ?: 384)
    }

    fun byte2int(src: ByteArray?): IntArray? {
        return if (src != null) {
            IntArray(src.size) { index -> src[index].toInt() }
        } else {
            null;
        }
    }

    private fun getBlackWhiteBitmap(bitmap: Bitmap): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val resultBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565)
        var color = 0
        var a: Int
        var r: Int
        var g: Int
        var b: Int
        var r1: Int
        var g1: Int
        var b1: Int
        val oldPx = IntArray(w * h)
        val newPx = IntArray(w * h)
        bitmap.getPixels(oldPx, 0, w, 0, 0, w, h)
        for (i in 0..(w * h)) {
            color = oldPx[i]
            r = Color.red(color)
            g = Color.green(color)
            b = Color.blue(color)
            a = Color.alpha(color)
            //黑白矩阵
            r1 = (0.33 * r + 0.59 * g + 0.11 * b).toInt()
            g1 = (0.33 * r + 0.59 * g + 0.11 * b).toInt()
            b1 = (0.33 * r + 0.59 * g + 0.11 * b).toInt()
            //检查各像素值是否超出范围
            if (r1 > 255) {
                r1 = 255
            }
            if (g1 > 255) {
                g1 = 255
            }
            if (b1 > 255) {
                b1 = 255
            }
            newPx[i] = Color.argb(a, r1, g1, b1)
        }
        resultBitmap.setPixels(newPx, 0, w, 0, 0, w, h)
        return resultBitmap
    }
}
