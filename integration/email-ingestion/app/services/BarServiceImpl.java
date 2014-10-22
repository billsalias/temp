package services;

import models.Bar;
import org.springframework.stereotype.Service;
import play.Logger;

import java.util.List;
import java.util.ArrayList;

@Service
public class BarServiceImpl implements BarService {

    @Override
    public List<Bar> getAllBars() {
    	Bar bar1 = new Bar();
    	bar1.id = "2";
    	bar1.name = "bar";
    	List<Bar> barList = new ArrayList();
    	barList.add(bar1);
        return barList;
    }

}