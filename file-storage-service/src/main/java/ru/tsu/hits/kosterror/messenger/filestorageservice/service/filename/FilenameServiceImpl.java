package ru.tsu.hits.kosterror.messenger.filestorageservice.service.filename;

import com.ibm.icu.text.Transliterator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilenameServiceImpl implements FilenameService {

    private final Transliterator transliterator;

    @Override
    public String convertToFilename(String rawFileName) {
        rawFileName = rawFileName.replace(" ", "_");
        return transliterator.transliterate(rawFileName);
    }

}
