import type { DictNameSearchResult } from 'types/api/dictionary';
import { useCallback } from 'react';
import { useNavigate, generatePath } from 'react-router-dom';
import { URL_PATH } from 'constants/index';

const useDictionaryNavigate = () => {
  const navigate = useNavigate();

  const goToDictDetailPage = useCallback((plantId: number) => {
    navigate(generatePath(URL_PATH.dictDetail, { id: plantId.toString() }));
  }, []);

  const goToProperDictPage = useCallback(
    (searchName: string, searchResults?: DictNameSearchResult[]) => {
      if (!searchName || !searchResults) return;

      const samePlant = searchResults.find(({ name }) => name === searchName);

      if (!samePlant) {
        navigate(`/dict?search=${searchName}`);
        return;
      }

      goToDictDetailPage(samePlant.id);
    },
    []
  );

  return { goToDictDetailPage, goToProperDictPage };
};

export default useDictionaryNavigate;
