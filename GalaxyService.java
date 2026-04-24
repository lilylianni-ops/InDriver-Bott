package com.galaxy.ultra;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.os.Bundle;
import java.util.List;

public class GalaxyService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode == null) return;

        // [001-010] AUTOMATIZACIÓN BÁSICA
        ejecutarFiltroDistancia(rootNode);
    }

    private void ejecutarFiltroDistancia(AccessibilityNodeInfo rootNode) {
        List<AccessibilityNodeInfo> kmNodes = rootNode.findAccessibilityNodeInfosByText("km");
        if (!kmNodes.isEmpty()) {
            String distText = kmNodes.get(0).getText().toString().replaceAll("[^0-9.]", "");
            float kms = Float.parseFloat(distText);

            if (kms >= 1 && kms <= 80) {
                // Función 6: Suma extra (+150)
                int precioFinal = 100 + (int)(kms * 75) + 150;
                
                // Función 5: Rango 100-6000
                if (precioFinal > 6000) precioFinal = 6000;

                // Función 4: Auto-Lapicero
                List<AccessibilityNodeInfo> editNodes = rootNode.findAccessibilityNodeInfosByViewId("com.indriver.drivers:id/edit_price_button");
                if (!editNodes.isEmpty()) {
                    editNodes.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    
                    try { Thread.sleep(350); } catch (InterruptedException e) {}

                    AccessibilityNodeInfo inputField = rootNode.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
                    if (inputField != null) {
                        Bundle args = new Bundle();
                        args.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, String.valueOf(precioFinal));
                        inputField.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args);
                    }
                }
            }
        }
    }

    @Override
    public void onInterrupt() {}
}
