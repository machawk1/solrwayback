/**
 * Transforms an array of harvest dates as epoch times into a suitable format for rendering a graph with iteration.
 * This is used to transform the API response into a usable format for VueJS.
 */
export function groupHarvestDatesByYearAndMonth(harvestDates, transformationFunction) {

    const fromDate = new Date(Math.min(...harvestDates));
    const toDate = new Date(Math.max(...harvestDates));

    // Parse the harvest dates into an array of Date objects.
    // Validate that the dates are integers.
    const parsedHarvestDates = harvestDates
        .filter(date => parseInt(date) !== NaN)
        .map(date => new Date(date));

    // Build an object with keys as the years.
    const yearRangeObject = buildYearRangeObject(fromDate, toDate);

    // Build Harvest Data Object.
    const harvestDataObject = addActivityLevelToDataObject(
        buildHarvestDataObject(yearRangeObject, parsedHarvestDates),
        transformationFunction
    );

    return {
        fromDate: fromDate,
        toDate: toDate,
        dates: harvestDataObject,
        numberOfHarvests: harvestDates.length
    }
}


/**
 * Build an object with keys as the years, e.g.
 * {
 *     2007: {},
 *     2008: {},
 *     ...
 * }
 */
function buildYearRangeObject(fromDate, toDate) {
    const yearRangeArray = buildYearRangeArray(fromDate, toDate);
    const yearRangeObject = {};

    for (let year of yearRangeArray) {
        yearRangeObject[year] = {};
    }  

    return yearRangeObject;
}


/**
 * Build an array of years from the minDate year to the maxDate year. E.g. [2007, 2008, 2009, 2010, 2011, ..., 2017]
 * minDate, maxDate are Date instances
 */
function buildYearRangeArray(minDate, maxDate) {
    return [...Array(maxDate.getFullYear() - minDate.getFullYear() + 1).keys()]     // e.g. [0, 1, 2, ..., 10]
        .map(year => year + minDate.getFullYear());                                                  // e.g. [2007, 2008, 2009, ..., 2017]
}


/**
 * Add months to the year range object.
 * Output is:
 * {
 *     2007: {
 *         1: {
 *             dates: [ ... ],
               numberOfHarvests: 5023
 *         },
 *         ...
 *     },
 *     ...
 * }
 */
function buildHarvestDataObject(yearRangeObject, parsedHarvestDates) {

    const arrayOfMonthValues = [...Array(12).keys()];       // [0, 1, 2, ..., 11]

    // Iterate over all years in the yearRangeObject
    for (let yearAsString of Object.keys(yearRangeObject)) {
        const year = parseInt(yearAsString);        // Since Object.keys() returns an array of strings, we need to convert years to a number.

        // Iterate over all months (0-11)
        for (let month of arrayOfMonthValues) {
            const allHarvestDatesInMonth = getHarvestsForMonth(year, month, parsedHarvestDates);

            yearRangeObject[year][month] = {
                days: buildDayObject(allHarvestDatesInMonth),
                numberOfHarvests: allHarvestDatesInMonth.length
            }
        }
    }

    return yearRangeObject;
}


/**
 * Return an array of all the parsedHarvestDates in the given year and month.
 */
function getHarvestsForMonth(year, month, parsedHarvestDates) {
    return parsedHarvestDates
        .filter(date => date.getMonth() === month && date.getFullYear() === year);
}


/**
 * Build an object of harvest dates for each day in the month.
 */
function buildDayObject(allHarvestDatesInMonth) {
    if (allHarvestDatesInMonth.length === 0) {
        return {};
    }

    // Sort the harvest date objects by time ascending.
    allHarvestDatesInMonth.sort((dateA, dateB) => dateA.getTime() - dateB.getTime());

    const daysInMonth = getDaysInMonth(allHarvestDatesInMonth[0]);
    const arrayOfDays = [...Array(daysInMonth).keys()].map(day => day + 1);     // [1, 2, ..., 31]

    // Initialise the object with days as key:
    const daysObject = {};

    for (let day of arrayOfDays) {
        daysObject[day] = [];
    }

    // Populate the daysObject with the harvestDates:
    for (let harvest of allHarvestDatesInMonth) {
        daysObject[harvest.getDate()].push(harvest);          
    }

    return daysObject;
}


/**
 * Given a Date object, return the number of days in the month.
 * Source: http://stackoverflow.com/questions/1184334/get-number-days-in-a-specified-month-using-javascript
 * 
 * It takes adds one to the month of the dateObject, but sets the day to 0. 
 * This gives the last day of the month of the dateObject.
 */
function getDaysInMonth(dateObject) {
    return new Date(dateObject.getFullYear(), dateObject.getMonth() + 1, 0).getDate();
}


/**
 * Calculates and adds an activityLevel property to each month,
 * this is used to color each table cell.
 * 
 * It takes a transformationFunction from activity-level.js. 
 * This enables easy changing of the transformation types.
 */
function addActivityLevelToDataObject(harvestDataObject, transformationFunction) {

    let {maximumYear, maximumMonth, maximumHarvests} = getMaximumHarvestCount(harvestDataObject);

    // Loop through each month and assign the activityLevel
    doForEachMonthInHarvestDataObject(harvestDataObject, (year, month) => {
        harvestDataObject[year][month].activityLevel = transformationFunction(harvestDataObject[year][month].numberOfHarvests, maximumHarvests);
    });

    return harvestDataObject;
}


/**
 * Loops through the data object, returns an object with 3 values: maximumYear, maximumMonth, maximumHarvests.
 */
function getMaximumHarvestCount(harvestDataObject) {

    let maximumYear = null;
    let maximumMonth = null;
    let maximumHarvests = 0;

    // Loop through each month in the data object, check if it beats the record for numberOfHarvests...
    doForEachMonthInHarvestDataObject(harvestDataObject, (year, month) => {
        if (harvestDataObject[year][month].numberOfHarvests >= maximumHarvests) {
            maximumHarvests = harvestDataObject[year][month].numberOfHarvests;
            maximumYear = year;
            maximumMonth = month;
        }
    });

    return {
        maximumYear: maximumYear, 
        maximumMonth: maximumMonth, 
        maximumHarvests: maximumHarvests
    };
}


/**
 * Higher-order function that loops through the harvestDataObject, calling a callback for each month.
 */
function doForEachMonthInHarvestDataObject(harvestDataObject, actionFunction) {

    for (let year of Object.keys(harvestDataObject)) {
        for (let month of Object.keys(harvestDataObject[year])) {
            actionFunction(year, month);
        }
    }
}


