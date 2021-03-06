package main.java.sda.web.services;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.faces.model.SelectItem;

@Service
@Scope("session")
public class AbstractModelService
{


    private SelectItem[] einkunfstarten = {new SelectItem("a01", "Eink�nfte aus Land- und Forstwirtschaft"),
            new SelectItem("b01", "Eink�nfte aus selbstst�ndiger Arbeit"),
            new SelectItem("c01", "Eink�nfte aus Gewerbebetrieb"),};

    public SelectItem[] getEinkunfstarten()
    {
        return einkunfstarten;
    }

}
