package com.br.projetoestagio.hubpitang.utils;

import java.io.Serializable;

public interface IObjectPersistent<T> extends Serializable {

    T getId();
}
