package org.improt.anticheat.text;

import org.improt.anticheat.settings.Settings;
import org.improt.anticheat.text.component.PlayerComponent;
import org.improt.anticheat.text.component.ReasonTextComponent;
import org.improt.anticheat.text.component.TranslationComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextTranslator {
    private List<TranslationComponent> translationComponents = new ArrayList<>();

    private Map<String, String> translations = new HashMap<>();

    public void init(Settings settings) {
        this.translations = settings.getTranslationMap();

        if (this.translations == null) {
            this.initDefaultTranslations();
        }

        // Create translation replace components
        registerComponent(new PlayerComponent());
        registerComponent(new ReasonTextComponent());
    }

    private void initDefaultTranslations() {
        this.translations = new HashMap<>();
        this.translations.put("anticheat.message.ban", "%player% \n (AC) You have been banned from the server: %reason%");
        this.translations.put("anticheat.message.kick", "%player% \n (AC) You have been kicked from the server: %reason%");
        this.translations.put("anticheat.message.notify", "(AC) %player% appears to be cheating (%reason%)");
        this.translations.put("anticheat.message.neural.notify", "(AC) {\"Teleport\", onClick:\"/tp %player%\"} %player% looks strange to me, could be cheating? (Ping: %ping%)"); //TODO: Implement Clickable component interpretation for this
    }

    @SuppressWarnings("unchecked")
    public String getFormattedTranslationFor(String key, Object...objects) {
        String translation = getTranslationFor(key);

        for (Object o : objects) {
            TranslationComponent component = getApplicableComponent(o);
            if (component != null) {
                translation = component.format(translation, o);
            }
        }

        return key;
    }

    private String getTranslationFor(String key) {
        return this.translations.get(key);
    }

    private TranslationComponent<?> getApplicableComponent(Object o) {
        for (TranslationComponent component : this.translationComponents) {
            if (component.typeMatches(o)) { //TODO: Test this
                return component;
            }
        }

        return null;
    }

    private void registerComponent(TranslationComponent component) {
        if (!translationComponents.contains(component)) {
            this.translationComponents.add(component);
        }
    }
}
