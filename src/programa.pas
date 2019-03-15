program uendel;
   var uendel: array[1 ..3] of boolean;
   var x: real;
   var luiz: real;
   var y: integer;
begin
	#x := 15 + 3.5 - 7.4*3;
    uendel[1] := 1 and false;
    #uendel := 133 >= 5;
    #if 32 and (2+2<>4) then
    #    begin
    #        x := x + 1;
    #        x := x * x;
    #        y := 1.4 * 7;
    #        uendel := 5 and false;
    #    end
    #else
    #    x := 0;
end.
