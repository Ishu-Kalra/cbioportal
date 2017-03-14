package org.cbioportal.persistence.mybatis;

import org.cbioportal.model.GeneGeneticAlteration;
import org.cbioportal.model.GenesetGeneticAlteration;
import org.cbioportal.persistence.GeneticDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GeneticDataMyBatisRepository implements GeneticDataRepository {

    @Autowired
    private GeneticDataMapper geneticDataMapper;

    @Override
    public String getCommaSeparatedSampleIdsOfGeneticProfile(String geneticProfileId) {

        return geneticDataMapper.getCommaSeparatedSampleIdsOfGeneticProfile(geneticProfileId);
    }

    @Override
    public List<GeneGeneticAlteration> getGeneticAlterations(String geneticProfileId, List<Integer> entrezGeneIds, 
                                                         String projection) {

        return geneticDataMapper.getGeneticAlterations(geneticProfileId, entrezGeneIds, projection);
    }

	@Override
	public List<GenesetGeneticAlteration> getGenesetAlterations(String geneticProfileId, List<String> genesetIds,
			String projection) {
		return geneticDataMapper.getGenesetAlterations(geneticProfileId, genesetIds, projection);
	}
}
