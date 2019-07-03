package ir.maxivity.tasbih.models;

import java.io.Serializable;

import ir.mirrajabi.persiancalendar.core.models.PersianDate;

public class PersianDateSerializable implements Serializable {

    public PersianDate date;

    public PersianDateSerializable(PersianDate date) {
        this.date = date;
    }
}
