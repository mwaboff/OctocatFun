Feature: GitHub User API Integration Tests

  Background:
    * url baseUrl
    * def expectedResponse = read('classpath:karate/fixtures/octocat-expected-response.json')

  Scenario: Get user details for valid username
    Given path '/api/github/user/octocat'
    When method GET
    Then status 200
    And match response == expectedResponse

  Scenario: Invalid username format returns 400
    Given path '/api/github/user/bad_user_name'
    When method GET
    Then status 400

  Scenario: Non-existent user returns 404
    Given path '/api/github/user/nonexistentuser123456'
    When method GET
    Then status 404

  Scenario: Username with hyphen is valid
    Given path '/api/github/user/test-user'
    When method GET
    Then status 200

  Scenario: Null username returns 400
    Given path '/api/github/user/'
    When method GET
    Then status 404

  Scenario: Empty username returns 404
    Given path '/api/github/user/ '
    When method GET
    Then status 400
