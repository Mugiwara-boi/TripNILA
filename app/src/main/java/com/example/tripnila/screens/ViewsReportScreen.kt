package com.example.tripnila.screens

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.tripnila.common.AppFilledButton2
import com.example.tripnila.common.AppTopBar
import com.example.tripnila.data.Business
import com.example.tripnila.data.Staycation1
import com.example.tripnila.data.Tour1
import com.example.tripnila.model.BusinessViewsViewModel

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewsReportScreen(
    businessViewsViewModel: BusinessViewsViewModel,
    reportType: String,
    onNavToBack: () -> Unit
) {

    val context = LocalContext.current
    var webView: WebView? = null

    val horizontalPaddingValue = 16.dp
    val verticalPaddingValue = 10.dp

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = rememberTopAppBarState())

    val staycationData by businessViewsViewModel.staycationDataMap.collectAsState()
    val tourData by businessViewsViewModel.tourDataMap.collectAsState()
    val business by businessViewsViewModel.business.collectAsState()
    val staycation by businessViewsViewModel.selectedStaycation.collectAsState()
    val tour by businessViewsViewModel.selectedTour.collectAsState()

    val viewsData by businessViewsViewModel.businessDataMap.collectAsState()
    val period by businessViewsViewModel.selectedPeriod.collectAsState()
    val month by businessViewsViewModel.selectedMonth.collectAsState()
    val year by businessViewsViewModel.selectedYear.collectAsState()
    val startMonth by businessViewsViewModel.selectedStartMonth.collectAsState()
    val endMonth by businessViewsViewModel.selectedEndMonth.collectAsState()
    val dateRange by businessViewsViewModel.dateRange.collectAsState()
    val isTourSelected by businessViewsViewModel.isTourSelected.collectAsState()

    val staycationTotalCollectedCommission by businessViewsViewModel.staycationTotalCollectedCommission.collectAsState()
    val staycationTotalPendingCommission by businessViewsViewModel.staycationTotalPendingCommission.collectAsState()
    val staycationTotalGrossSale by businessViewsViewModel.staycationTotalGrossSale.collectAsState()

    val tourTotalCollectedCommission by businessViewsViewModel.tourTotalCollectedCommission.collectAsState()
    val tourTotalPendingCommission by businessViewsViewModel.tourTotalPendingCommission.collectAsState()
    val tourTotalGrossSale by businessViewsViewModel.tourTotalGrossSale.collectAsState()

    val reportHeader = when(reportType) {
        "viewsReport" -> "$period Views Report"
        else -> {"Unregistered Report Type"}
    }

    val dateHeader = when(period) {
        "Monthly" -> "$month $year"
        "Bi-yearly" -> "$startMonth - $endMonth $year"
        "Yearly" -> "Year $year"
        else -> "Unknown Error"
    }

    if(isTourSelected){
        businessViewsViewModel.setTotalGrossSales(tourTotalGrossSale)
        businessViewsViewModel.setTotalCollectedCommission(tourTotalCollectedCommission)
        businessViewsViewModel.setTotalPendingCommission(tourTotalPendingCommission)
    } else if(!isTourSelected){
        businessViewsViewModel.setTotalGrossSales(staycationTotalGrossSale)
        businessViewsViewModel.setTotalCollectedCommission(staycationTotalCollectedCommission)
        businessViewsViewModel.setTotalPendingCommission(staycationTotalPendingCommission)
    }
    val totalGrossSalesUnformat by businessViewsViewModel.totalGrossSale.collectAsState()
    val totalCollectedCommissionUnformat by businessViewsViewModel.totalCollectedCommission.collectAsState()
    val totalPendingCommissionUnformat by businessViewsViewModel.totalPendingCommission.collectAsState()

    val totalGrossSales = "₱ %.2f".format(totalGrossSalesUnformat)
    val totalCollectedCommission = "₱ %.2f".format(totalCollectedCommissionUnformat)
    val totalPendingCommission = "₱ %.2f".format(totalPendingCommissionUnformat)
    val totalNetSales = "₱ %.2f".format(totalGrossSalesUnformat - (totalPendingCommissionUnformat + totalCollectedCommissionUnformat))

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Scaffold(
            topBar = {
                AppTopBar(
                    headerText = "Reports",
                    scrollBehavior = scrollBehavior,
                    color = Color.White,
                    navigationIcon = {
                        IconButton(onClick = { onNavToBack() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {


                AppFilledButton2(
                    modifier = Modifier
                        .padding(horizontalPaddingValue, verticalPaddingValue)
                        .align(Alignment.End),
                    buttonText = "Export",
                    onClick = {
                        exportAsPdf(reportHeader, webView = webView, context = context)
                    }
                )


                AndroidView(
                    modifier = Modifier.padding(horizontalPaddingValue, verticalPaddingValue),
                    factory = { context ->
                        WebView(context)
                            .apply {
                                webViewClient = WebViewClient()
                                settings.javaScriptEnabled = true
                                loadDataWithBaseURL(null,
                                    getHtmlContent(staycationData, tourData, reportHeader,
                                        dateHeader, dateRange, totalGrossSales, totalCollectedCommission,
                                        totalPendingCommission, isTourSelected, staycation, tour, totalNetSales, business
                                    ),
//                                    getHtmlContent(staycationData, tourData, reportHeader,
//                                        dateHeader, dateRange),
                                    "text/html",
                                    "UTF-8",
                                    null)
                            }
                    },
                ) { view ->
                    webView = view
                    view.webViewClient = WebViewClient()
                    view.settings.javaScriptEnabled = true
                    view.loadDataWithBaseURL(null,
                        getHtmlContent(staycationData, tourData, reportHeader,
                            dateHeader, dateRange, totalGrossSales, totalCollectedCommission,
                            totalPendingCommission,isTourSelected, staycation, tour, totalNetSales, business
                        ),
                        "text/html",
                        "UTF-8",
                        null)
                }

            }
        }


    }
}

private fun getHtmlContent(
    staycationData: List<Map<String, String>>,
    tourData: List<Map<String, String>>,
    reportHeader: String,
    dateHeader: String,
    dateRange: String,
    totalGrossSales: String,
    totalCollectedCommission: String,
    totalPendingCommission: String,
    isTourSelected: Boolean,
    staycation: Staycation1,
    tour: Tour1,
    totalNetSales: String,
    business: Business,
): String {

    return """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Report</title>
            <style>
                
        
                span {
                    width: 100%;
                }
        
                #address-date-range {
                    display: flex;
                    justify-content: space-between;
                }
        
                #date-range {
                    text-align: end;
                }
        
                #report-id, #report-date {
                    text-align: center;
                }
        
                #table-container {
                    overflow-x: auto; /* Enable horizontal scrolling */
                }
        
                table {
                    border-collapse: collapse;
                    width: 100%;
                }
        
                th, td {
                    border: 1px solid #000000;
                    text-align: left;
                    padding: 8px;
                }
        
                th {
                    background-color: #f2f2f2;
                }
        
                .signature-line {
                    text-align: center; /* Align content in the middle */
                }
        
                .signature-line p {
                    margin: 0; /* Remove default margin */
                }
        
                .signature-line p:first-child {
                    width: 300px; /* Adjust width as needed */
                    border-top: 1px solid black; /* Add border to create the line */
                }
        
            </style>
        </head>
        <body>
            <h1 id="report-id">Views Report</h1>
            <h3 id="report-date">2024</h3>
        
            <h2>${business.businessTitle}</h2>
            
            <div id="contact">
                <div id="address-date-range">
                    <span id="date-range">2024</span>
                </div>
            </div>
            
        
            <div id="table-container">
                <table>
                    <thead>
                        <tr>
                            <th></th>
                            <th>Booking Id</th>
                            <th>Tourist Name</th>
                            <th>Number of Guests</th>
                            <th>Completion Date</th>
                            <th>Booking Date</th>
                            <th>Booking Status</th>
                            <th>Gross Booking Sales</th>
                            <th>Collected Commission</th>
                            <th>Pending Commission</th>
                        </tr>
                    </thead>
                    <tbody id="table-body">
                        <!-- Table rows will be dynamically added here -->
                    </tbody>
                </table>
            </div>
            
        
            <br><br><br>
            <div>
            <h4>Total Net Sales</h4>
                <p>$totalNetSales</p>
            </div>
            <br><br><br>
            <div class="signature-line">
                <p>Signed by</p>
            </div>
        
           <script>
                // Sample data for the table
                var staycationData = ${staycationData.joinToString(separator = ",", prefix = "[", postfix = "]") {
        it.entries.joinToString(separator = ",", prefix = "{", postfix = "}") { (key, value) ->
            """"$key":"$value""""
        }
    }}
            
                var tourData = ${tourData.joinToString(separator = ",", prefix = "[", postfix = "]") {
        it.entries.joinToString(separator = ",", prefix = "{", postfix = "}") { (key, value) ->
            """"$key":"$value""""
        }
    }}
            
                var totalGrossSales = "$totalGrossSales";
                var totalCollectedCommission = "$totalCollectedCommission";
                var totalPendingCommission = "$totalPendingCommission";
            
                function populateTable() {
                    var tableBody = document.getElementById("table-body");
                    tableBody.innerHTML = ""; // Clear existing rows
            
                    if ($isTourSelected) { // Check if tour data should be included
                        var tourRow = document.createElement("tr");
            
                        // Loop to create 7 columns and empty their content except for the first column
                        for (var i = 0; i < 10; i++) {
                            var cell = document.createElement("td");
                            if (i === 0) {
                                var boldText = document.createElement("strong");
                                boldText.textContent = "Tour";
                                cell.appendChild(boldText);
                            }
                            tourRow.appendChild(cell);
                        }
                        tableBody.appendChild(tourRow);
            
                        tourData.forEach(function(item) {
                            var row = document.createElement("tr");
                            var isFirstColumn = true; // Flag to track if it's the first column
                            Object.values(item).forEach(function(value) {
                                var cell = document.createElement("td");
                                if (isFirstColumn) {
                                    // Skip populating data in the first column
                                    isFirstColumn = false; // Reset the flag for the next row
                                } else {
                                    cell.textContent = value;
                                }
                                row.appendChild(cell);
                            });
                            tableBody.appendChild(row);
                        });
                    } else { // Show staycation data when tour is not selected
                        var staycationRow = document.createElement("tr");
            
                        // Loop to create 7 columns and empty their content except for the first column
                        for (var i = 0; i < 10; i++) {
                            var cell = document.createElement("td");
                            if (i === 0) {
                                var boldText = document.createElement("strong");
                                boldText.textContent = "Staycation";
                                cell.appendChild(boldText);
                            }
                            staycationRow.appendChild(cell);
                        }
                        tableBody.appendChild(staycationRow);
            
                        staycationData.forEach(function(item) {
                            var row = document.createElement("tr");
                            var isFirstColumn = true; // Flag to track if it's the first column
                            Object.values(item).forEach(function(value) {
                                var cell = document.createElement("td");
                                if (isFirstColumn) {
                                    // Skip populating data in the first column
                                    isFirstColumn = false; // Reset the flag for the next row
                                } else {
                                    cell.textContent = value;
                                }
                                row.appendChild(cell);
                            });
                            tableBody.appendChild(row);
                        });
                    }
            
                    var totalRow = document.createElement("tr");
            
                    // Loop to create 7 columns and empty their content except for the first column
                    for (var i = 0; i < 10; i++) {
                        var cell = document.createElement("td");
                        if (i === 0) {
                            var boldText = document.createElement("strong");
                            boldText.textContent = "Total";
                            cell.appendChild(boldText);
                        } else if (i === 7) {
                            var boldText = document.createElement("strong");
                            boldText.textContent = totalGrossSales;
                            cell.appendChild(boldText);
                        } else if (i === 8) {
                            var boldText = document.createElement("strong");
                            boldText.textContent = totalCollectedCommission;
                            cell.appendChild(boldText);
                        } else if (i === 9) {
                            var boldText = document.createElement("strong");
                            boldText.textContent = totalPendingCommission;
                            cell.appendChild(boldText);
                        }
                        totalRow.appendChild(cell);
                    }
                    tableBody.appendChild(totalRow);
                }
            
                // Call the function to populate the table when the page loads
                window.onload = populateTable;
            </script>
        </body>
        </html>
    """
}


@Preview
@Composable
private fun GeneratedReportScreenPreview() {
    // GeneratedReportScreen()
}