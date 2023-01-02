package io.github.fuadreza.imin_printer

import android.app.Activity
import android.content.Context
import androidx.annotation.NonNull
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

/** IminPrinterPlugin */
class IminPrinterPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var context: Context
  private lateinit var activity: Activity

  private var connectType = PrintConnectType.USB

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "imin_printer")
    context = flutterPluginBinding.applicationContext
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else if(call.method == "initPrinter") {
      val deviceModel = SystemPropManager.getModel()
      if (deviceModel.contains("M")) {
        connectType = IminPrintUtils.PrintConnectType.SPI
      }
      IminPrintUtils.getInstance(activity).initPrinter(connectType)
      result.success("init")
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity;
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
