package com.ex.movierater.messaging;

import java.io.IOException;

public interface ReviewReceiver {
    void receive(String reviewJson) throws IOException;
}
