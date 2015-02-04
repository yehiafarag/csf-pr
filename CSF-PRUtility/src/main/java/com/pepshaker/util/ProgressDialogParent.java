/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pepshaker.util;

/**
 * Interface that makes it simpler to let the cancellation of the progress bar
 * propagate to the parent frame or dialog that opened the progress bar.
 *
 * @author Harald Barsnes
 */
public interface ProgressDialogParent {

    /**
     * Cancel the process in the frame or dialog that opened the progress bar.
     */
    public void cancelProgress();
}
