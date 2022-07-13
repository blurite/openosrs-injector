package com.openosrs.injector.injectors.raw;

import com.openosrs.injector.InjectUtil;
import com.openosrs.injector.injection.InjectData;
import com.openosrs.injector.injectors.AbstractInjector;
import net.runelite.asm.attributes.code.Instruction;
import net.runelite.asm.attributes.code.Instructions;
import net.runelite.asm.Method;
import net.runelite.asm.attributes.code.instructions.NewArray;
import net.runelite.asm.attributes.code.instructions.SiPush;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

/**
 * @author Kris | 13/07/2022
 */
public class VarpArray extends AbstractInjector {
    private static final Logger logger = Logging.getLogger(VarpArray.class);

    public VarpArray(InjectData inject) {
        super(inject);
    }

    @Override
    public void inject() {
        logger.lifecycle("Attempting to replace varp array.");
        final Method method = InjectUtil.findMethod(inject, "<clinit>", "Varps");


        Instructions ins = method.getCode().getInstructions();
        for (Instruction i : ins.getInstructions()) {
            if (i instanceof NewArray) {
                int index = ins.getInstructions().indexOf(i);
                if (((NewArray) i).getArrayType() == 10) {
                    Instruction arg1 = ins.getInstructions().get(index - 1);
                    if (arg1 instanceof SiPush) {
                        SiPush push = (SiPush) arg1;
                        Object constant = push.getConstant();
                        if (constant instanceof Short) {
                            if (constant.equals((short) 4000)) {
                                push.setOperand((short) 15000);
                                logger.lifecycle("Found an int array with the size 4000, changing to 15000.");
                            }
                        }
                    }
                }
            }
        }
    }
}
