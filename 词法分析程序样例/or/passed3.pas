BEGIN
    I := 1;
    J := 50;
    X12Z := 0;
    Z := 111;
    WHILE I <= 100 DO
        BEGIN
            IF I < J + 5 THEN
                J := J + 5
            ELSE
                X12Z := X12Z + Z;
            I := I + 1
        END;
    S1234 := 10 + 20 * (I + 0);
    A := 10;
    IF 0 <> J THEN
        J := (A * (I + 4) + J) * 2
    ELSE
        J := 0
END
