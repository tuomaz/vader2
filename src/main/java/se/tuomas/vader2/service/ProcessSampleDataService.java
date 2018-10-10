package se.tuomas.vader2.service;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Comparator;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import se.tuomas.vader2.domain.SensorSample;

@Service
public class ProcessSampleDataService {
    @Autowired
    private MessageSource messageSource;

    Locale locale = LocaleContextHolder.getLocale();

    public List<SensorSample> processSamples(final List<SensorSample> samples) {
        for (SensorSample sample : samples) {
            // Fix names
            String realName = messageSource.getMessage(sample.getName(), null, "", locale);
            if (StringUtils.isBlank(realName)) {
                realName = sample.getName();
            }
            sample.setRealName(realName);

            // Check timestamp
            long currentTime = System.currentTimeMillis();
            long recentMillis = currentTime - (1000 * 60 * 30);
            Date recent = new Date(recentMillis);
            if (sample.getTimestamp().before(recent)) {
                sample.setOld(true);
            }
            String[] group = sample.getName().split("-", 2);
            if (group.length > 0 && group[0] != null) {
                sample.setGroup(group[0]);
                String niceName = messageSource.getMessage(group[0], null, "", locale);
                if (StringUtils.isBlank(niceName)) {
                    niceName = group[0];
                }
            sample.setGroupNice(niceName);
            }
        }

        Collections.sort(samples, new Comparator<SensorSample>() {
            @Override
            public int compare(SensorSample a, SensorSample b) {
                return a.getRealName().compareTo(b.getRealName());
            }
        });

        return samples;
    }
}
