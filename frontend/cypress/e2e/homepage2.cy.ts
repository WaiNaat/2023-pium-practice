describe('피움 메인 페이지2', () => {
  beforeEach(() => {
    cy.visit('http://localhost:8282');
  });

  it('피움 소개 문구가 보인다2.', () => {
    cy.contains('식물을 쉽게');
    cy.get('img[alt="logo"]');
    cy.contains('피움에 등록된 식물을 검색해 보세요');
  });

  it('검색창을 이용해 식물을 검색할 수 있다2.', () => {
    cy.get('input').type('아카시').wait(300);
    cy.contains('아카시');
    cy.contains('아카시아');
  });
});
