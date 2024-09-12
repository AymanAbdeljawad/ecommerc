// This is your test publishable API key.
const stripe = Stripe(publicKey);

// The items the customer wants to buy
const request = {
    amount: amount,
    email: email,
    productName: productName
}

let elements;

initialize();
checkStatus();

document
    .querySelector("#payment-form")
    .addEventListener("submit", handleSubmit);

let emailAddress = '';
// Fetches a payment intent and captures the client secret

let paymentIntentID = '';
async function initialize() {
    const response = await fetch("/create-payment-intent", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(request),
    });

    const { intentID, clientSecret } = await response.json();

    paymentIntentID = intentID;

    const appearance = {
        theme: 'stripe',
    };
    elements = stripe.elements({ appearance, clientSecret });

    const linkAuthenticationElement = elements.create("linkAuthentication");
    linkAuthenticationElement.mount("#link-authentication-element");

    linkAuthenticationElement.on('change', (event) => {
        emailAddress = event.value.email;
    });

    const paymentElementOptions = {
        layout: "tabs",
        defaultValues: {
            billingDetails:{
                email: request.email
            }
        }
    };

    const paymentElement = elements.create("payment", paymentElementOptions);
    paymentElement.mount("#payment-element");
}

console.log(paymentIntentID);
//--------------------------------------------------------------------------
async function handleSubmit(e) {
    e.preventDefault();  // منع الإرسال الافتراضي للنموذج
    setLoading(true);  // بدء تحميل

    // تأكيد الدفع
    const { error } = await stripe.confirmPayment({
        elements,
        confirmParams: {
            // تأكد من تحديث هذا الرابط إلى صفحة إتمام الدفع الخاصة بك
            return_url: `http://localhost:8084/payment-success?amount=${encodeURIComponent(amount)}&productName=${encodeURIComponent(productName)}`,
            receipt_email: emailAddress
        },
    });

    // معالجة الأخطاء المحتملة
    if (error) {
        if (error.type === "card_error" || error.type === "validation_error") {
            showMessage(error.message);  // عرض رسالة الخطأ المناسبة
        } else {
            showMessage("An unexpected error occurred.");  // رسالة خطأ غير متوقعة
        }
    } else {
        // هذا النقطة ستكون محققة إذا لم يكن هناك خطأ مباشر عند تأكيد الدفع
        // سيتم إعادة توجيه المستخدم إلى `return_url`
    }

    setLoading(false);  // إيقاف تحميل
}

// دالة لعرض الرسائل للمستخدم
function showMessage(message) {
    const messageContainer = document.getElementById('message-container');
    if (messageContainer) {
        messageContainer.textContent = message;
    }
}
//---------------------------------------------------------------------------












//async function handleSubmit(e) {
//    e.preventDefault();
//    setLoading(true);
//
//    const { error, paymentIntent } = await stripe.confirmPayment({
//        elements,
//        confirmParams: {
//            // تأكد من تحديث هذا الرابط إلى صفحة إتمام الدفع الخاصة بك
//            return_url: `http://localhost:8091/payment-success?amount=${encodeURIComponent(amount)}&productName=${encodeURIComponent(productName)}`,
//            receipt_email: emailAddress
//        },
//    });
//
//    if (error) {
//        // التعامل مع الأخطاء
//        console.error("Payment error:", error);
//        alert("Payment failed: " + error.message);
//    } else {
//        // عملية الدفع تمت بنجاح، المستخدم سيتم توجيهه إلى `return_url`
//    }
//}




//---------------------------------------------------------------
//async function handleSubmit(e) {
//    e.preventDefault();
//    setLoading(true);
//
//    const { error } = await stripe.confirmPayment({
//        elements,
//        confirmParams: {
//            // Make sure to change this to your payment completion page
////            return_url: "https://dashboard.stripe.com/test/payments/"+paymentIntentID,
//            return_url: "http://localhost:8091/payment-success?amount=15&productName=asd",
//            receipt_email: emailAddress
//        },
//    });
//
//    // This point will only be reached if there is an immediate error when
//    // confirming the payment. Otherwise, your customer will be redirected to
//    // your `return_url`. For some payment methods like iDEAL, your customer will
//    // be redirected to an intermediate site first to authorize the payment, then
//    // redirected to the `return_url`.
//    if (error.type === "card_error" || error.type === "validation_error") {
//        showMessage(error.message);
//    } else {
//        showMessage("An unexpected error occurred.");
//    }
//
//    setLoading(false);
//}
//-----------------------------------------------------------------------------------



// Fetches the payment intent status after payment submission
async function checkStatus() {
    const clientSecret = new URLSearchParams(window.location.search).get(
        "sk_test_51Px8vARwubGTClfzrxK2GxYh99sBJ6qpeyKqajIDWHIDchL0qVHgAoRPLq3PTFxTzIYp2yeBOZz4ZikISoZesMBR00EwOqyybL"
    );

    if (!clientSecret) {
        return;
    }

    const { paymentIntent } = await stripe.retrievePaymentIntent(clientSecret);

    switch (paymentIntent.status) {
        case "succeeded":
            showMessage("Payment succeeded!");
            break;
        case "processing":
            showMessage("Your payment is processing.");
            break;
        case "requires_payment_method":
            showMessage("Your payment was not successful, please try again.");
            break;
        default:
            showMessage("Something went wrong.");
            break;
    }
}

// ------- UI helpers -------

function showMessage(messageText) {
    const messageContainer = document.querySelector("#payment-message");

    messageContainer.classList.remove("hidden");
    messageContainer.textContent = messageText;

    setTimeout(function () {
        messageContainer.classList.add("hidden");
        messageContainer.textContent = "";
    }, 4000);
}

// Show a spinner on payment submission
function setLoading(isLoading) {
    if (isLoading) {
        // Disable the button and show a spinner
        document.querySelector("#submit").disabled = true;
        document.querySelector("#spinner").classList.remove("hidden");
        document.querySelector("#button-text").classList.add("hidden");
    } else {
        document.querySelector("#submit").disabled = false;
        document.querySelector("#spinner").classList.add("hidden");
        document.querySelector("#button-text").classList.remove("hidden");
    }
}