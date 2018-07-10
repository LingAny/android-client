package ru.tp.lingany.lingany;

import net.orange_box.storebox.annotations.method.KeyByString;

public interface Settings {

    @KeyByString("authIdentityString")
    String getAuthIdentityString();

    @KeyByString("authIdentityString")
    void setAuthIdentityString(String value);

}
