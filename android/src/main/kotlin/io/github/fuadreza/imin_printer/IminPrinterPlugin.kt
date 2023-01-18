package io.github.fuadreza.imin_printer

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
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
import android.graphics.Typeface

/** IminPrinterPlugin */
class IminPrinterPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var instance: IminPrintUtils

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
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else if (call.method == "initPrinter") {
            val deviceModel = SystemPropManager.getModel()
            val printSize = arguments?.get("printSize") as Int?
            if (deviceModel.contains("M")) {
                connectType = PrintConnectType.SPI
            }
            instance.initPrinter(connectType)
            instance.setAlignment(0)
            instance.setTextSize(19)
            instance.setTextTypeface(Typeface.MONOSPACE);
            instance.setTextStyle(Typeface.NORMAL)
            instance.setTextWidth(printSize ?: 384)
            result.success("init")
        } else if (call.method == "getStatus") {
            val status: Int = instance.getPrinterStatus(connectType)
            result.success(String.format("%d", status))
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
        } else {
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
}
