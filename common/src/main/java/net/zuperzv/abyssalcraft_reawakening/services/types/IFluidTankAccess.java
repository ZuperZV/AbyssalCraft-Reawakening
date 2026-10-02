package net.zuperzv.abyssalcraft_reawakening.services.types;

import net.zuperzv.abyssalcraft_reawakening.commonCode.fluid.EssenceBoilerFluid;

public interface IFluidTankAccess {
    EssenceBoilerFluid getFluidTank();

    int getFluidTankAmount();

    int getFluidTankCapacity();

    int fillFluidTank(EssenceBoilerFluid incoming);

    EssenceBoilerFluid drainFluidTank(int amount);
}
