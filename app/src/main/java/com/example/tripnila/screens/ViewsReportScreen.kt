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

    val business by businessViewsViewModel.business.collectAsState()

    val businessDataMap by businessViewsViewModel.businessDataMap.collectAsState()

    val insightsSelectedYear by businessViewsViewModel.insightsSelectedYear.collectAsState()

    val reportHeader = "Business Views Report"

    val year = if (insightsSelectedYear == "All") {
        "2023-2024"
    } else {
        insightsSelectedYear
    }


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
                                    getHtmlContent(businessDataMap,year, business),
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
                        getHtmlContent(businessDataMap,year, business),
                        "text/html",
                        "UTF-8",
                        null)
                }

            }
        }


    }
}

private fun getHtmlContent(
    businessData: List<Map<String, String>>,
    year: String,
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
            <h3 id="report-date">$year</h3>
        
            <h2>${business.businessTitle}</h2>
            
            <div id="contact">
                <div id="address-date-range">
                    <span id="date-range">$year</span>
                </div>
            </div>
            
        
            <div id="table-container">
                <table>
                    <thead>
                        <tr>
                            <th></th>
                            <th>Month</th>
                            <th>Number of Views</th>
                        </tr>
                    </thead>
                    <tbody id="table-body">
                        <!-- Table rows will be dynamically added here -->
                    </tbody>
                </table>
            </div>
            
        
            <br><br><br>
            <div class="signature-line">
                <p>Signed by</p>
            </div>
        
           <script>
                // Sample data for the table

                var businessData = ${businessData.joinToString(separator = ",", prefix = "[", postfix = "]") {
                    it.entries.joinToString(separator = ",", prefix = "{", postfix = "}") { (key, value) ->
                        """"$key":"$value""""
                    }
                }}

                function populateTable() {
                    var tableBody = document.getElementById("table-body");
                    tableBody.innerHTML = ""; // Clear existing rows
        
                    var tourRow = document.createElement("tr");
            
                        // Loop to create 7 columns and empty their content except for the first column
                        for (var i = 0; i < 3; i++) {
                            var cell = document.createElement("td");
                            if (i === 0) {
                                var boldText = document.createElement("strong");
                                boldText.textContent = "Business";
                                cell.appendChild(boldText);
                            }
                            tourRow.appendChild(cell);
                        }
                        tableBody.appendChild(tourRow);
            
                        businessData.forEach(function(item) {
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