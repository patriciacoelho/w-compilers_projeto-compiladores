program uendel;
   var uendel: array[1 .. 5] of array[1 ..5] of boolean;
   var x: real;
   var luiz: boolean;
begin
    uendel[0][0] := true;
    luiz := true or false;
    if(3>=2) or (2+2<>4) then
        begin
            x := x + 1;
        end
    else 
        x := 0;
end.
