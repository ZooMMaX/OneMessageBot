package space.zoommax.viewGroup;

import com.pengrad.telegrambot.model.request.LabeledPrice;
import com.pengrad.telegrambot.request.SendInvoice;
import lombok.Builder;
import space.zoommax.utils.keyboard.Keyboard;

import java.util.List;

@Builder
public class InvoiceMessage implements ViewMessage {
    private long chatId;
    private long messageThreadId;
    private String title;
    private String description;
    private String payload;
    private String providerToken;
    private String currency;
    private List<LabeledPrice> prices;
    private int maxTipAmount;
    private List<Integer> suggestedTipAmounts;
    private String startParameter;
    private String providerData;
    private String photoUrl;
    private int photoSize;
    private int photoWidth;
    private int photoHeight;
    private boolean needName;
    private boolean needPhoneNumber;
    private boolean needEmail;
    private boolean needShippingAddress;
    private boolean sendPhoneNumberToProvider;
    private boolean sendEmailToProvider;
    private boolean isFlexible;
    private boolean disableNotification;
    private boolean protectContent;
    private boolean allowPaidBrodcast;
    private String messageEffectId;
    private Keyboard keyboard;
    @Builder.Default
    private String onMessageFlag =  "";
    @Builder.Default
    private boolean notify = false;
    private String viewMessageToUpdate;
    @Builder.Default
    private boolean needUpdate = false;
    @Builder.Default
    private long updateTime = 0;
    @Override
    public void run() {
        SendInvoice sendInvoice = new SendInvoice(chatId, title, description, payload, currency, prices.toArray(new LabeledPrice[0]));
        if (messageThreadId != 0) {
            sendInvoice.messageThreadId(Math.toIntExact(messageThreadId));
        }
        if (providerToken != null) {
            sendInvoice.providerToken(providerToken);
        }
        if (maxTipAmount != 0) {
            sendInvoice.maxTipAmount(maxTipAmount);
        }
        if (suggestedTipAmounts != null) {
            sendInvoice.suggestedTipAmounts(suggestedTipAmounts.toArray(new Integer[0]));
        }
        if (startParameter != null) {
            sendInvoice.startParameter(startParameter);
        }
        if (providerData != null) {
            sendInvoice.providerData(providerData);
        }
        if (photoUrl != null) {
            sendInvoice.photoUrl(photoUrl);
        }
        if (photoSize != 0) {
            sendInvoice.photoSize(photoSize);
        }
        if (photoWidth != 0) {
            sendInvoice.photoWidth(photoWidth);
        }
        if (photoHeight != 0) {
            sendInvoice.photoHeight(photoHeight);
        }
        if (needName) {
            sendInvoice.needName(needName);
        }
        if (needPhoneNumber) {
            sendInvoice.needPhoneNumber(needPhoneNumber);
        }
        if (needEmail) {
            sendInvoice.needEmail(needEmail);
        }
        if (needShippingAddress) {
            sendInvoice.needShippingAddress(needShippingAddress);
        }
        if (sendPhoneNumberToProvider) {
            sendInvoice.sendPhoneNumberToProvider(sendPhoneNumberToProvider);
        }
        if (sendEmailToProvider) {
            sendInvoice.sendEmailToProvider(sendEmailToProvider);
        }
        if (isFlexible) {
            sendInvoice.isFlexible(isFlexible);
        }
        if (disableNotification) {
            sendInvoice.disableNotification(disableNotification);
        }
        if (protectContent) {
            sendInvoice.protectContent(protectContent);
        }
        if (allowPaidBrodcast) {
            sendInvoice.allowPaidBroadcast(allowPaidBrodcast);
        }
        if (messageEffectId != null) {
            sendInvoice.messageEffectId(messageEffectId);
        }

        sendInvoice(sendInvoice, onMessageFlag, notify, chatId, keyboard);
    }
}
