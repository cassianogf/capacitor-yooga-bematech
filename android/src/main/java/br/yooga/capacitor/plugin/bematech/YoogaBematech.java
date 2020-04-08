package br.yooga.capacitor.plugin.bematech;

import android.webkit.WebView;
import android.widget.TextView;

import com.getcapacitor.JSArray;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.bematech.android.miniprinter.Alignment;
import br.com.bematech.android.miniprinter.LineFeed;
import br.com.bematech.android.miniprinter.LineThickness;
import br.com.bematech.android.miniprinter.NetworkPrinterInfo;
import br.com.bematech.android.miniprinter.Printer;
import br.com.bematech.android.miniprinter.PrinterCommands;
import br.com.bematech.android.miniprinter.PrinterModel;
import br.com.bematech.android.miniprinter.Receipt;
import br.com.bematech.android.miniprinter.barcode.QRCode;

@NativePlugin()
public class YoogaBematech extends Plugin {

    //===============================================================
    // PRINT
    //===============================================================
    private WebView mWebView;
    private TextView txtPrinterInfo = null;

    @PluginMethod()
    public void testPrint(PluginCall call) {
        PrinterModel type = PrinterModel.MP4200TH;
        String host = "";
        String mac = "";
        Integer port = 9100;

        try {
            JSONObject config = new JSONObject(call.getObject("config").toString());
            /*if(config.getString("type").equals("MP4200TH")) {
                type = PrinterModel.MP4200TH;
            }*/

            host = config.getString("host");
            port = config.getInt("port");
            mac = config.getString("mac");
        } catch(Exception e) {
            e.printStackTrace();
        }


        try {
            printer = new Printer(getContext(), new NetworkPrinterInfo(type, host, port, mac));
            btnPrintTextOnClicked(call.getArray("acoes"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //===================================================================================
    // BEMATECH
    //===================================================================================
    private Printer printer;

    public void btnPrintTextOnClicked(JSArray array) throws IOException {
        try {
            Receipt receipt = new Receipt(37);

            for (Object a : array.toList()) {
                JSONObject json = new JSONObject(a.toString());

                String metodo = json.getString("metodo");
                String value = json.getString("value");

                switch(metodo) {
                    case "addText":
                        receipt.addText(value);
                        break;
                    case "addTextHtmlFormatting":
                        receipt.addTextHtmlFormatting(value);
                        break;
                    case "QRCode":
                        QRCode qrCode = new QRCode(value);
                        qrCode.setAlignment(Alignment.CENTER);

                        receipt.addBarcode(qrCode);
                        break;
                    case "addCommand":
                        switch(value) {
                            case "getAlignmentCenter":
                                receipt.addCommand(PrinterCommands.getAlignmentCenter());
                                break;

                            case "getAlignmentRight":
                                receipt.addCommand(PrinterCommands.getAlignmentRight());
                                break;

                            case "LF":
                                receipt.addCommand(PrinterCommands.LF);
                                break;

                            case "getCondensedOn":
                                receipt.addCommand(PrinterCommands.getCondensedOn());
                                break;

                            case "getCondensedOff":
                                receipt.addCommand(PrinterCommands.getCondensedOff());
                                break;

                            case "getAlignmentLeft":
                                receipt.addCommand(PrinterCommands.getAlignmentLeft());
                                break;

                            case "getHorizontalLine":
                                receipt.addCommand(PrinterCommands.getHorizontalLine(LineThickness.THIN,1,1));
                                break;

                            case "getFineLineFeed":
                                receipt.addCommand(PrinterCommands.getFineLineFeed(LineFeed.ONE_MILLIMETER));
                                break;

                            case "getPartialPaperCut":
                                receipt.addCommand(PrinterCommands.getPartialPaperCut());
                                break;
                        }


                        break;
                }

                System.out.println(json.getString("metodo"));
            }

            printer.printReceipt(receipt);
            receipt.clear();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
