package me.FusionDev.FusionPixelmon.apis;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;

public class CauseStackUtil {
    public static Cause createCause(Object object) {
        if (Sponge.getServer().isMainThread()) {
            try (CauseStackManager.StackFrame stackFrame = Sponge.getCauseStackManager().pushCauseFrame()) {
                stackFrame.pushCause(object);
                return Sponge.getCauseStackManager().getCurrentCause();
            }
        }
        return Cause.builder().append(object).build(EventContext.empty());
    }
}
