# MockGiven

MockGiven is an aggregation of Mockito, JGiven and AssertJ tools. It simplifies writing tests, which heavily depend on mocking.

### Why?

Because I'm too lazy for writing 4 separate classes for testing a simple implementation.

For me using JGiven was just too much and while JGiven surely generates nice reports, it felt like using an excavator to plant flowers.  
So as a result I combined 3 tools that I use the most and made another tool (duh) that makes it easy to create tests.

### Benefits

 * Nice reports thanks to JGiven
 * Mocking thanks to Mockito
 * Fluent assertions thanks to AssertJ
 * No need to declare given stages for mocking
 * No need to declare when stage for running a single line of code
 * No need to declare then stage for wrapping assertions
 
### Examples

Taken from mockgiven-junit-example

```Java
    @Mock
    private NumberProvider numberProvider;

    @InjectMocks
    private QuirkyCalculator quirkyCalculator;

    @Test
    public void numberShouldBeExponentiated() {
        given("a number provider", numberProvider.provide()).returns(BigInteger.TEN);
        when("a number is exponentiated to the power of 2").by(() -> quirkyCalculator.exponentiate(2));
        then().assertUsing(BigIntegerAssert.class)
              .thatResult()
              .isEqualTo(100);
    }

```

and this generates following report

```text
 Number should be exponentiated

   Given a number provider returns 10
    When a number is exponentiated to the power of 2
    Then result is equal to 100
```

###FAQ
####Is it stable?

No, I'm still testing it and thinking of new features, create a feature request if you have any good ideas.

Also, testing it would help.

####Why should we stick to this instead of JGiven?

You maybe shouldn't. Or ought to. It's not like my tool is any better than JGiven, it simplifies some things for me and I think it can help you too.

####What if Mockito or AssertJ aren't enough for my given and then?

Just use MockScenarioTest and create "traditional" JGiven tests.

####Why you don't support JUnit 5?

Because JGiven doesn't support JUnit Jupiter, the only support it has is *experimental*.


### License

This software is licensed under [Mozilla Public License Version 2.0](LICENSE)